package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.CouponView
import kr.hhplus.be.server.domain.coupon.model.UserCouponView
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
) {

    fun getUserCouponBy(couponCommand: CouponCommand.UserCoupon): UserCouponView =
        UserCouponView.from(
            couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
                ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        )

    fun getCouponBy(couponCommand: CouponCommand.Coupon): CouponView =
        CouponView.from(
            couponRepository.findCouponBy(couponCommand.couponId)
                ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        )

    fun issue(couponCommand: CouponCommand.Issue): UserCouponView {

        val coupon = couponRepository.findCouponWithLockBy(couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        coupon.issue()

        val userCoupon =
            couponRepository.saveUserCoupon(UserCoupon(couponId = coupon.id, userId = couponCommand.userId))
        return UserCouponView.from(userCoupon)
    }

    fun validateUse(couponCommand: CouponCommand.UserCoupon): UserCouponView {

        val userCoupon = couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        userCoupon.validateUsable()
        return UserCouponView.from(userCoupon)
    }

    fun use(couponCommand: CouponCommand.UseCoupon): UserCouponView {

        var userCoupon = couponRepository.findUserCouponWithLockBy(couponCommand.userCouponId)
            ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        userCoupon.use()
        userCoupon = couponRepository.updateUserCoupon(userCoupon)
        return UserCouponView.from(userCoupon)
    }
}