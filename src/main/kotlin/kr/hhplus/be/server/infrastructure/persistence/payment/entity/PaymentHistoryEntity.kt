package kr.hhplus.be.server.infrastructure.persistence.payment.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import java.time.LocalDateTime

@Entity
@Table(name = "payment_histories")
class PaymentHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "payment_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_payment_status_history_payment")
    )
    val payment: PaymentEntity,

    @Column(name = "pay_total_price", nullable = false)
    val payTotalPrice: Int,

    @Column(name = "discount_price", nullable = false)
    val discountPrice: Int,

    @Column(name = "origin_total_price", nullable = false)
    val originTotalPrice: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val status: PaymentStatus,

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)