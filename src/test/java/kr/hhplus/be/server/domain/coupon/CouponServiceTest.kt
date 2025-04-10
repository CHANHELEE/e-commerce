package kr.hhplus.be.server.domain.coupon
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.UserCoupon
import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    lateinit var couponRepository: CouponRepository

    @InjectMocks
    lateinit var couponService: CouponService

    private val userId = 1L
    private val couponId = 10L
    private val command = CouponCommand.UserCoupon(userId, couponId)

    @Nested
    inner class GetUserCouponBy {

        @Test
        fun `사용자 쿠폰이 존재하면 반환된다`() {
            // given
            val userCoupon = UserCoupon(id = 1L, userId = userId, couponId = couponId, usedAt = null)
            given(couponRepository.findUserCouponBy(userId, couponId)).willReturn(userCoupon)

            // when
            val result = couponService.getUserCouponBy(command)

            // then
            assertThat(result).isEqualTo(userCoupon)
        }

        @Test
        fun `사용자 쿠폰이 존재하지 않으면 BusinessException(USER_COUPON_NOT_EXIST)예외가 발생한다`() {
            // given
            given(couponRepository.findUserCouponBy(userId, couponId)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.getUserCouponBy(command)
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        }
    }

    @Nested
    inner class GetUserCouponWithLockBy {

        @Test
        fun `사용자 쿠폰이 존재하면 반환된다`() {
            // given
            val userCoupon = UserCoupon(id = 2L, userId = userId, couponId = couponId, usedAt = null)
            given(couponRepository.findUserCouponWithLockBy(userId, couponId)).willReturn(userCoupon)

            // when
            val result = couponService.getUserCouponWithLockBy(command)

            // then
            assertThat(result).isEqualTo(userCoupon)
        }

        @Test
        fun `사용자 쿠폰이 존재하지 않으면 BusinessException(USER_COUPON_NOT_EXIST)예외가 발생한다`() {
            // given
            given(couponRepository.findUserCouponWithLockBy(userId, couponId)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.getUserCouponWithLockBy(command)
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        }
    }
}