package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CouponTest {

    @Nested
    inner class Issue {

        @Test
        fun `쿠폰 수량이 남아 있으면 1 감소한다`() {
            // given
            val coupon = Coupon(id = 1L, amount = 3L, discountPrice = 1000L, name = "테스트쿠폰")

            // when
            coupon.issue()

            // then
            assertThat(coupon.amount).isEqualTo(2L)
        }

        @Test
        fun `쿠폰 수량이 0이면 BusinessException(COUPON_OUT_OF_AMOUNT)예외가 발생한다`() {
            // given
            val coupon = Coupon(id = 1L, amount = 0L, discountPrice = 1000L, name = "테스트쿠폰")

            // when & then
            val exception = assertThrows<BusinessException> {
                coupon.issue()
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.COUPON_OUT_OF_AMOUNT)
        }

        @Test
        fun `쿠폰 수량이 1일 때 발급 시 0이 되고 예외 없이 성공한다`() {
            // given
            val coupon = Coupon(id = 1L, amount = 1L, discountPrice = 1000L, name = "테스트쿠폰")

            // when
            coupon.issue()

            // then
            assertThat(coupon.amount).isEqualTo(0L)
        }
    }
}