package kr.hhplus.be.server.domain.coupon.model

import java.time.LocalDateTime

class CouponCommand {

    data class UserCoupon(
        val userId: Long,
        val couponId: Long,
    )

    data class UseCoupon(
        val couponId: Long,
        val userId: Long,
        val usedAt: LocalDateTime
    )
}