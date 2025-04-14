package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.entity.Coupon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    lateinit var couponRepository: CouponRepository

    @InjectMocks
    lateinit var couponService: CouponService

    private val userId = 1L
    private val couponId = 10L
    private val command = CouponCommand.UserCoupon(userId, couponId)
    private val userCouponId = 100L

    @Nested
    inner class GetUserCouponBy {

        @Test
        fun `사용자 쿠폰이 존재하면 반환된다`() {
            // given
            val userCoupon = UserCoupon(id = 1L, userId = userId, couponId = couponId, usedAt = null)
            given(couponRepository.findUserCouponBy(userId, couponId)).willReturn(userCoupon)

            // when
            val result = couponService.getUserCouponBy(command)

            //then
            assertThat(result)
                .extracting("id", "userId", "couponId")
                .contains(userCoupon.id, userCoupon.userId, userCoupon.couponId)
            verify(couponRepository, times(1)).findUserCouponBy(any(), any())
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
    inner class GetCouponBy {

        @Test
        fun `쿠폰이 존재하면 반환한다`() {
            // given
            val coupon = Coupon(
                id = 100L,
                name = "할인쿠폰",
                amount = 10000L,
                discountPrice = 1000L
            )
            val command = CouponCommand.Coupon(couponId = 100L)

            given(couponRepository.findCouponBy(100L)).willReturn(coupon)

            // when
            val result = couponService.getCouponBy(command)

            // then
            assertThat(result)
                .extracting("id", "amount")
                .contains(coupon.id, coupon.amount)
            verify(couponRepository, times(1)).findCouponBy(any())
        }

        @Test
        fun `쿠폰이 존재하지 않으면 예외를 던진다`() {
            // given
            given(couponRepository.findCouponBy(999L)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.getCouponBy(CouponCommand.Coupon(couponId = 999L))
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.COUPON_NOT_EXIST)
        }
    }

    @Nested
    inner class Issue {

        @Test
        fun `쿠폰이 존재하고 수량이 남아있으면 사용자 쿠폰이 정상 발급된다`() {
            // given
            val coupon = Coupon(id = 1L, name = "할인쿠폰", amount = 5L, discountPrice = 1000L)
            val userCoupon = UserCoupon(id = 1L, userId = 10L, couponId = 1L)
            val command = CouponCommand.Issue(userId = 10L, couponId = 1L)

            given(couponRepository.findCouponWithLockBy(1L)).willReturn(coupon)
            given(couponRepository.saveUserCoupon(any())).willReturn(userCoupon)

            // when
            val result = couponService.issue(command)

            // then
            assertThat(result)
                .extracting("id", "userId", "couponId")
                .contains(userCoupon.id, userCoupon.userId, userCoupon.couponId)
            verify(couponRepository, times(1)).findCouponWithLockBy(any())
            verify(couponRepository, times(1)).saveUserCoupon(any())
        }

        @Test
        fun `쿠폰이 존재하지 않으면 예외가 발생한다`() {
            // given
            val command = CouponCommand.Issue(userId = 10L, couponId = 999L)
            given(couponRepository.findCouponWithLockBy(999L)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.issue(command)
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.COUPON_NOT_EXIST)
        }

        @Test
        fun `쿠폰 수량이 0이면 BusinessException(COUPON_OUT_OF_AMOUNT)예외가 발생한다`() {
            // given
            val coupon = Coupon(id = 1L, name = "할인쿠폰", amount = 0L, discountPrice = 1000L)
            val command = CouponCommand.Issue(userId = 10L, couponId = 1L)
            given(couponRepository.findCouponWithLockBy(1L)).willReturn(coupon)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.issue(command)
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.COUPON_OUT_OF_AMOUNT)
        }
    }

    @Nested
    inner class ValidateUse {

        @Test
        fun `사용 가능한 쿠폰이면 UserCouponView 를 반환한다`() {
            // given
            val userCoupon = UserCoupon(id = userCouponId, userId = userId, couponId = couponId, usedAt = null)
            given(couponRepository.findUserCouponBy(userId, couponId)).willReturn(userCoupon)

            // when
            val result = couponService.validateUse(CouponCommand.UserCoupon(userId, couponId))

            // then
            assertThat(result.couponId).isEqualTo(couponId)
            verify(couponRepository, times(1)).findUserCouponBy(userId, couponId)
        }

        @Test
        fun `쿠폰이 존재하지 않으면 BusinessException(USER_COUPON_NOT_EXIST)예외 발생`() {
            // given
            given(couponRepository.findUserCouponBy(userId, couponId)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.validateUse(CouponCommand.UserCoupon(userId, couponId))
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        }
    }

    @Nested
    inner class Use {

        @Test
        fun `쿠폰을 정상적으로 사용할 수 있다`() {
            // given
            val userCoupon = UserCoupon(id = userCouponId, userId = userId, couponId = couponId, usedAt = null)
            given(couponRepository.findUserCouponWithLockBy(userCouponId)).willReturn(userCoupon)
            given(couponRepository.updateUserCoupon(any())).willReturn(userCoupon)

            // when
            val result = couponService.use(CouponCommand.UseCoupon(userCouponId))

            // then
            assertThat(result.couponId).isEqualTo(couponId)
            verify(couponRepository, times(1)).findUserCouponWithLockBy(userCouponId)
            verify(couponRepository, times(1)).updateUserCoupon(any())
        }

        @Test
        fun `쿠폰이 존재하지 않으면 BusinessException(USER_COUPON_NOT_EXIST)예외 발생`() {
            // given
            given(couponRepository.findUserCouponWithLockBy(userCouponId)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                couponService.use(CouponCommand.UseCoupon(userCouponId))
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        }
    }
}