package kr.hhplus.be.server.infrastructure.persistence.payment.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import kr.hhplus.be.server.domain.payment.model.entity.PaymentHistory
import kr.hhplus.be.server.infrastructure.persistence.common.entity.HistoryBaseEntity

@Entity
@Table(name = "payment_histories")
class PaymentHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "payment_id", nullable = false)
    val paymentId: Long,

    @Column(name = "pay_total_price", nullable = false)
    val payTotalPrice: Long,

    @Column(name = "discount_price")
    val discountPrice: Long?,

    @Column(name = "origin_total_price", nullable = false)
    val originTotalPrice: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val status: PaymentStatus,

    ) : HistoryBaseEntity() {

    fun toDomain(): PaymentHistory {
        return PaymentHistory(
            id = id,
            paymentId = paymentId,
            payTotalPrice = payTotalPrice,
            originTotalPrice = originTotalPrice,
            discountPrice = discountPrice,
            status = status,
            createdAt = createdAt,
        )
    }

    companion object {
        fun from(paymentHistory: PaymentHistory): PaymentHistoryEntity {
            return PaymentHistoryEntity(
                id = paymentHistory.id,
                paymentId = paymentHistory.paymentId,
                payTotalPrice = paymentHistory.payTotalPrice,
                originTotalPrice = paymentHistory.originTotalPrice,
                discountPrice = paymentHistory.discountPrice,
                status = paymentHistory.status,
            )
        }
    }
}