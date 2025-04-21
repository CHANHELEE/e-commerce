package kr.hhplus.be.server.domain.coupon.model

import java.time.LocalDateTime

class CouponCommand {

    data class UserCoupon(
        val userId: Long,
        val couponId: Long,
    )

    data class UseCoupon(
        val userCouponId: Long,
    )

    data class Coupon(
        val couponId: Long,
    )

    data class Issue(
        val couponId: Long,
        val userId: Long,
    )

    data class UpdateUserCoupon(
        val id: Long,
        val usedAt: LocalDateTime,
    )
}