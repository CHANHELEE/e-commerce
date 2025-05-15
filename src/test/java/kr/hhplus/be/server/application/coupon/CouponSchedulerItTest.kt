package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueKeyPrefix
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.jvm.optionals.getOrNull

class CouponSchedulerItTest : IntegrationTestSupport() {

    @Test
    fun `쿠폰 요청 큐로부터 데이터를 가져와 RDB 적재에 성공한다`() {

        //given
        val coupon = couponJpaRepository.save(
            CouponEntity(
                amount = 1L,
                discountPrice = 1_000L,
                name = "test"
            )
        )

        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        val key = "${CouponIssueKeyPrefix.ISSUE_TARGET.prefix}${coupon.id}"
        redisTemplate.delete(key)
        redisTemplate.opsForList().leftPush(key, user.id.toString())

        // when
        couponScheduler.issueCoupons()

        // then
        val remaining = redisTemplate.opsForList().size(key)
        assertThat(0L).isEqualTo(remaining)

        val foundUser = userJpaRepository.findById(user.id!!).getOrNull()
        assertThat(foundUser!!.id).isEqualTo(user.id!!)
    }
}