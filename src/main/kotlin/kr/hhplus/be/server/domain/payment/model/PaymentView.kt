package kr.hhplus.be.server.domain.payment.model

import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import kr.hhplus.be.server.domain.payment.model.entity.Payment
import java.time.LocalDateTime

data class PaymentView(
    val id: Long,
    val orderId: Long,
    val originTotalPrice: Long,
    val payTotalPrice: Long,
    val status: PaymentStatus,
    val discountPrice: Long?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: Payment): PaymentView {
            return PaymentView(
                id = entity.id,
                orderId = entity.orderId,
                originTotalPrice = entity.originTotalPrice,
                payTotalPrice = entity.payTotalPrice,
                discountPrice = entity.discountPrice,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                status = entity.status
            )
        }
    }
}
