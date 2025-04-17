package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.entity.Coupon
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon

interface CouponRepository {

    fun findUserCouponWithLockBy(userCouponId: Long): UserCoupon?

    fun findUserCouponBy(userId: Long, couponId: Long): UserCoupon?

    fun saveUserCoupon(userCoupon: UserCoupon): UserCoupon

    fun findCouponBy(couponId: Long): Coupon?

    fun findCouponWithLockBy(couponId: Long): Coupon?

    fun saveCoupon(coupon: Coupon): Coupon
}