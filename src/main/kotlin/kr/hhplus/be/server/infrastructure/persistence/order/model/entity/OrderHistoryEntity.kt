package kr.hhplus.be.server.infrastructure.persistence.order.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.infrastructure.persistence.common.entity.HistoryBaseEntity

@Entity
@Table(name = "orders_histories")
class OrderHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val orderId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val status: OrderStatus,
) : HistoryBaseEntity() {

    fun toDomain(): OrderHistory {
        return OrderHistory(
            id = id,
            orderId = orderId,
            orderStatus = status,
            createdAt = createdAt
        )
    }

    companion object {
        fun from(orderHistory: OrderHistory): OrderHistoryEntity {
            return OrderHistoryEntity(
                orderId = orderHistory.orderId,
                status = orderHistory.orderStatus,
            )
        }

        fun from(orderEntity: OrderEntity): OrderHistoryEntity {
            return OrderHistoryEntity(
                orderId = orderEntity.id,
                status = orderEntity.status,
            )
        }
    }
}