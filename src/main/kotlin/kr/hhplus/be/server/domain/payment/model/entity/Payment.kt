package kr.hhplus.be.server.domain.payment.model.entity

import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import java.time.LocalDateTime

class Payment(
    val id: Long = 0,
    val orderId: Long,
    val originTotalPrice: Long,
    var status: PaymentStatus,
    var payTotalPrice: Long,
    var discountPrice: Long? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)