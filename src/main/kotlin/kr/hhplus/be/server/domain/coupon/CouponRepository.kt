package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.Coupon
import kr.hhplus.be.server.domain.coupon.model.UpdateUserCoupon
import kr.hhplus.be.server.domain.coupon.model.UserCoupon

interface CouponRepository {

    fun findUserCouponWithLockBy(userId: Long, couponId: Long): UserCoupon?

    fun findUserCouponBy(userId: Long, couponId: Long): UserCoupon?

    fun saveUserCoupon(userCoupon: UserCoupon): UserCoupon

    fun updateUserCoupon(updateUserCoupon: UpdateUserCoupon): UserCoupon

    fun findCouponBy(couponId: Long): Coupon?

    fun findCouponWithLockBy(couponId: Long): Coupon?
}