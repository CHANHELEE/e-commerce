package kr.hhplus.be.server.domain.coupon.model

import java.time.LocalDateTime

class Coupon(
    val id: Long = 0,
    val amount: Long,
    val discountPrice: Long,
    val name: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)