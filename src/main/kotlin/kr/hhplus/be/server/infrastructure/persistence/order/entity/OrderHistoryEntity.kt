package kr.hhplus.be.server.infrastructure.persistence.order.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.infrastructure.persistence.common.entity.HistoryBaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "orders_histories")
class OrderHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_order_history_order")
    )
    val order: OrderEntity,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val status: OrderStatus,
) : HistoryBaseEntity()