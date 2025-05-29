package kr.hhplus.be.server.presentation.coupon.model

class CouponResponse {

    data class Coupon(
        val id: Long,
        val amount: Long,
        val name: String,
        val discountPrice: Long,
    )

    data class Issue(
        val requestId: String,
    )
}