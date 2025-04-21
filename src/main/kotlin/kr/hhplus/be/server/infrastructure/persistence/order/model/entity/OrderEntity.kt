package kr.hhplus.be.server.infrastructure.persistence.order.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "user_coupon_id", nullable = true)
    var userCouponId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: OrderStatus,

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)", nullable = true)
    var deletedAt: LocalDateTime? = null

) : BaseEntity() {

    fun toDomain(): Order {
        return Order(
            id = id,
            userId = userId,
            userCouponId = userCouponId,
            status = status,
            deletedAt = deletedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun from(order: Order): OrderEntity {
            return OrderEntity(
                id = order.id,
                userId = order.userId,
                userCouponId = order.userCouponId,
                status = order.status,
                deletedAt = order.deletedAt
            )
        }
    }
}