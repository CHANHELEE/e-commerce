package kr.hhplus.be.server.domain.coupon.model.event

import kr.hhplus.be.server.common.BusinessException

data class CouponIssueFailedEvent(
    val requestId: String,
    val couponId: Long,
    val exception: BusinessException,
)
