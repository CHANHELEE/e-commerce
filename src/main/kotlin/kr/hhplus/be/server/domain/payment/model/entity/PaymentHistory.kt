package kr.hhplus.be.server.domain.payment.model.entity

import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import java.time.LocalDateTime

class PaymentHistory(
    val id: Long = 0,
    val paymentId: Long,
    val originTotalPrice: Long,
    val status: PaymentStatus,
    val payTotalPrice: Long,
    val discountPrice: Long? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
)