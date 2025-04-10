package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.UserCoupon
import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.Coupon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.then
import java.time.LocalDateTime

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

    @Nested
    inner class UpdateUserCoupon {

        @Test
        fun `사용자 쿠폰이 정상적으로 업데이트된다`() {
            // given
            val now = LocalDateTime.now()
            val command = CouponCommand.UseCoupon(userCouponId = 1L, usedAt = now)
            val expected = UserCoupon(
                id = 1L,
                userId = 10L,
                couponId = 100L,
                usedAt = now
            )

            given(couponRepository.updateUserCoupon(any())).willReturn(expected)

            // when
            val result = couponService.updateUserCoupon(command)

            // then
            then(couponRepository).should().updateUserCoupon(check {
                assertThat(it.id).isEqualTo(command.userCouponId)
                assertThat(it.usedAt).isEqualTo(command.usedAt)
            })

            assertThat(result).isEqualTo(expected)
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
            assertThat(result).isEqualTo(coupon)
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
            assertThat(result).isEqualTo(userCoupon)
            assertThat(coupon.amount).isEqualTo(4L) // issue()로 인해 감소 확인
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
}