package kr.hhplus.be.server.infrastructure.persistence.payment.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import kr.hhplus.be.server.domain.payment.model.entity.Payment
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(
    name = "payment",
    uniqueConstraints = [UniqueConstraint(name = "uk_payment_order", columnNames = ["order_id"])]
)
class PaymentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "order_id", nullable = false)
    val orderId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: PaymentStatus,

    @Column(name = "origin_total_price", nullable = false)
    var originTotalPrice: Long,

    @Column(name = "pay_total_price", nullable = false)
    var payTotalPrice: Long,

    @Column(name = "discount_price", nullable = true)
    var discountPrice: Long? = null

) : BaseEntity() {

    fun toDomain(): Payment {
        return Payment(
            id = id,
            orderId = orderId,
            status = status,
            originTotalPrice = originTotalPrice,
            payTotalPrice = payTotalPrice,
            discountPrice = discountPrice,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun from(payment: Payment): PaymentEntity {
            return PaymentEntity(
                id = payment.id,
                orderId = payment.orderId,
                status = payment.status,
                originTotalPrice = payment.originTotalPrice,
                payTotalPrice = payment.payTotalPrice,
                discountPrice = payment.discountPrice,
            )
        }
    }
}