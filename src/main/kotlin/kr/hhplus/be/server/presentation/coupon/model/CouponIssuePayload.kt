package kr.hhplus.be.server.presentation.coupon.model

import kr.hhplus.be.server.domain.coupon.model.CouponCommand

class CouponIssuePayload(
    val userId: Long,
    val requestId: String
) {
    companion object {
        fun from(couponCommand: CouponCommand.RequestIssue, requestId: String): CouponIssuePayload {
            return CouponIssuePayload(
                userId = couponCommand.userId,
                requestId = requestId,
            )
        }
    }
}