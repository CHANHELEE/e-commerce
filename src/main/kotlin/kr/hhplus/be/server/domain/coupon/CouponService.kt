package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
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

}