package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueKeyPrefix
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class CouponServiceItTest : IntegrationTestSupport() {

    @Nested
    inner class Issue {

        @Test
        fun `쿠폰 발급 후 사용자 쿠폰 정보가 조회된다`() {

            // given
            val coupon = couponJpaRepository.save(
                CouponEntity(
                    amount = 10,
                    discountPrice = 1000,
                    name = "테스트쿠폰"
                )
            )
            val userId = 100L
            val issueCommand = CouponCommand.Issue(userId = userId, couponId = coupon.id)

            // when
            val result = couponService.issue(issueCommand)

            // then
            assertEquals(coupon.id, result.couponId)
            assertEquals(userId, result.userId)
        }

        @Test
        fun `동시성 테스트 - 쿠폰 재고 만큼 발급 되어야한다`() {

            // given
            val couponAmount = 10L
            val coupon = couponJpaRepository.save(
                CouponEntity(
                    amount = couponAmount,
                    discountPrice = 1000,
                    name = "테스트쿠폰"
                )
            )
            val count = coupon.amount.toInt() + 100
            val executorService = Executors.newFixedThreadPool(count)
            val latch = CountDownLatch(count)
            val successCount = AtomicInteger(0)

            // when
            repeat(count) { i ->
                executorService.submit {
                    try {
                        couponService.issue(
                            CouponCommand.Issue(
                                couponId = coupon.id,
                                userId = i.toLong()
                            )
                        )
                        successCount.incrementAndGet()
                    } finally {
                        latch.countDown()
                    }

                }
            }

            latch.await()

            // then
            val resultCoupon = couponJpaRepository.findByIdOrNull(coupon.id)!!
            assertThat(successCount.get()).isEqualTo(couponAmount)
            assertThat(resultCoupon.amount).isEqualTo(0)
        }

        @Test
        fun `발급된 쿠폰은 정상적으로 사용할 수 있다`() {

            val coupon = couponJpaRepository.save(
                CouponEntity(
                    amount = 10,
                    discountPrice = 1000,
                    name = "테스트쿠폰"
                )
            )
            val userId = 100L
            val issueCommand = CouponCommand.Issue(userId = userId, couponId = coupon.id)
            val userCoupon = couponService.issue(issueCommand)

            val useCommand = CouponCommand.UseCoupon(userCouponId = userCoupon.id)
            val used = couponService.use(useCommand)

            assertEquals(userCoupon.id, used.id)
            assertEquals(true, used.usedAt != null)
        }
    }

    @Nested
    inner class RequestIssue {

        @Test
        fun `1명의 사용자가 쿠폰 발급 요청에 성공한다`() {

            // given
            val coupon = couponJpaRepository.save(
                CouponEntity(
                    amount = 1,
                    discountPrice = 1000,
                    name = "단건 쿠폰"
                )
            )
            val userId = 1L

            redisTemplate.opsForValue().set("${CouponIssueKeyPrefix.COUPON_AMOUNT.prefix}${coupon.id}", "1")

            // when
            val result = couponService.requestIssue(CouponCommand.Issue(userId = userId, couponId = coupon.id))

            // then
            assertThat(result.userId).isEqualTo(userId)
            assertThat(result.couponId).isEqualTo(coupon.id)

            val queueKey = "${CouponIssueKeyPrefix.ISSUE_TARGET.prefix}${coupon.id}"
            val list = redisTemplate.opsForList().range(queueKey, 0, -1)!!

            assertThat(list).containsExactly(userId.toString())
        }

        @Test
        fun `동시성테스트 - 50개의 쿠폰에 1000명이 동시에 요청하면 50명만 요청 큐에 저장된다`() {

            // given
            val couponAmount = 50L
            val coupon = couponJpaRepository.save(
                CouponEntity(
                    amount = couponAmount,
                    discountPrice = 1000,
                    name = "동시성 쿠폰"
                )
            )
            val totalUser = 1000
            val executor = Executors.newFixedThreadPool(20)
            val latch = CountDownLatch(totalUser)

            redisTemplate.opsForValue().set("${CouponIssueKeyPrefix.COUPON_AMOUNT.prefix}${coupon.id}", couponAmount.toString())

            // when
            repeat(totalUser) { i ->
                executor.submit {
                    try {
                        couponService.requestIssue(
                            CouponCommand.Issue(userId = i.toLong(), couponId = coupon.id)
                        )
                    } catch (_: Exception) {
                        // 무시: 재고 초과 or 중복
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()

            // then
            val queueKey = "${CouponIssueKeyPrefix.ISSUE_TARGET.prefix}${coupon.id}"
            val result = redisTemplate.opsForList().range(queueKey, 0, -1)!!

            assertThat(result.size).isEqualTo(couponAmount)
            assertThat(result.distinct().size).isEqualTo(couponAmount)
        }
    }
}