package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import java.time.LocalDateTime

class UserCouponTest {

    @Test
    fun `이미 사용된 쿠폰은 validateUsable에서 BusinessException(USER_COUPON_ALREADY_USED)예외가 발생한다`() {
        val coupon = UserCoupon(
            id = 1L,
            couponId = 100L,
            userId = 200L,
            usedAt = LocalDateTime.now()
        )

        //when
        val exception = assertThrows<BusinessException> {
            coupon.validateUsable()
        }

        //then
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_COUPON_ALREADY_USED)
    }

    @Test
    fun `사용되지 않은 쿠폰은 use 호출 시 usedAt이 세팅된다`() {
        val coupon = UserCoupon(
            id = 1L,
            couponId = 100L,
            userId = 200L,
            usedAt = null
        )

        coupon.use()

        assertThat(coupon.usedAt).isNotNull()
    }

    @Test
    fun `이미 사용된 쿠폰은 use 호출 시 BusinessException(USER_COUPON_ALREADY_USED)예외가 발생한다`() {
        val coupon = UserCoupon(
            id = 1L,
            couponId = 100L,
            userId = 200L,
            usedAt = LocalDateTime.now()
        )

        //when
        val exception = assertThrows<BusinessException> {
            coupon.use()
        }

        //then
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_COUPON_ALREADY_USED)
    }
}