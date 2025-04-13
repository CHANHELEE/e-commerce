package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.Coupon
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.UpdateUserCoupon
import kr.hhplus.be.server.domain.coupon.model.UserCoupon
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
) {

    fun getUserCouponBy(couponCommand: CouponCommand.UserCoupon): UserCoupon =
        couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)

    fun getUserCouponWithLockBy(couponCommand: CouponCommand.UserCoupon): UserCoupon =
        couponRepository.findUserCouponWithLockBy(couponCommand.userId, couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)

    fun updateUserCoupon(couponCommand: CouponCommand.UseCoupon): UserCoupon =
        couponRepository.updateUserCoupon(UpdateUserCoupon(couponCommand.userCouponId, couponCommand.usedAt))

    fun getCouponBy(couponCommand: CouponCommand.Coupon): Coupon =
        couponRepository.findCouponBy(couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)

    fun issue(couponCommand: CouponCommand.Issue): UserCoupon {

        val coupon = couponRepository.findCouponWithLockBy(couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        coupon.issue()

        val userCoupon =
            couponRepository.saveUserCoupon(UserCoupon(couponId = coupon.id, userId = couponCommand.userId))
        return userCoupon
    }
}