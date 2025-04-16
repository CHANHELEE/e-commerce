package kr.hhplus.be.server.infrastructure.persistence.payment.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "payment",
    uniqueConstraints = [UniqueConstraint(name = "uk_payment_order", columnNames = ["order_id"])]
)
class PaymentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "order_id", nullable = false)
    val orderId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: PaymentStatus,

    @Column(name = "origin_total_price", nullable = false)
    var originTotalPrice: Int,

    @Column(name = "pay_total_price", nullable = false)
    var payTotalPrice: Int,

    @Column(name = "discount_price", nullable = true)
    var discountPrice: Int? = null

) : BaseEntity()