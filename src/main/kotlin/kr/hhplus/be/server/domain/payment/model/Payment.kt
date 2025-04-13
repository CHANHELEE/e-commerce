package kr.hhplus.be.server.domain.payment.model

import java.time.LocalDateTime

class Payment(
    val id: Long = 0,
    val orderId: Long,
    val originTotalPrice: Long,
    val payTotalPrice: Long,
    val discountPrice: Long? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)