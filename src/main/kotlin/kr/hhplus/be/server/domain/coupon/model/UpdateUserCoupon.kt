package kr.hhplus.be.server.domain.coupon.model

import java.time.LocalDateTime

data class UpdateUserCoupon(
    val id: Long,
    val usedAt: LocalDateTime,
)
