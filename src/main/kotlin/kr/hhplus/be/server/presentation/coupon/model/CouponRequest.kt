package kr.hhplus.be.server.presentation.coupon.model

class CouponRequest {

    data class Issue(
        var userId: Long,
        var couponId: Long,
    )
}