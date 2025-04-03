package kr.hhplus.be.server.presentation.coupon.model

class CouponRequest {

    data class Issue(
        val userId: Long,
        val couponId: Long,
    )
}