package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
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

    @Autowired
    lateinit var couponService: CouponService

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
        fun `동시성 테스트 - 쿠폰 재고 만큼 발급 되어야한다 (동시성 제어 코드 미적용으로 실패)`() {

            // given
            val coupon = couponJpaRepository.save(
                CouponEntity(
                    amount = 10,
                    discountPrice = 1000,
                    name = "테스트쿠폰"
                )
            )
            val count = coupon.amount.toInt() + 10
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
            assertThat(successCount.get()).isEqualTo(10)
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
}