package kr.hhplus.be.server.presentation.coupon.model

class CouponResponse {

    data class Coupon(
        val id: Long,
        val amount: Long,
        val name: String,
    )

    data class Issue(
        val userId: Long,
        val couponId: Long,
    )
}