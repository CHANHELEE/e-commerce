package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
import java.time.LocalDateTime

data class OrderView(
    val id: Long,
    val userId: Long,
    val userCouponId: Long?,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: Order): OrderView {
            return OrderView(
                id = entity.id,
                userId = entity.userId,
                userCouponId = entity.userCouponId,
                status = entity.status,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                deletedAt = entity.deletedAt
            )
        }
    }
}

data class OrderProductView(
    val id: Long,
    val productOptionId: Long,
    val productId: Long,
    val orderId: Long,
    val productPrice: Long,
    val quantity: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: OrderProduct): OrderProductView {
            return OrderProductView(
                id = entity.id,
                productOptionId = entity.productOptionId,
                productId = entity.productId,
                orderId = entity.orderId,
                productPrice = entity.productPrice,
                quantity = entity.quantity,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                deletedAt = entity.deletedAt
            )
        }
    }
}

data class OrderHistoryView(
    val id: Long = 0,
    val orderId: Long,
    val orderStatus: OrderStatus,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(entity: OrderHistory): OrderHistoryView {
            return OrderHistoryView(
                id = entity.id,
                orderId = entity.orderId,
                orderStatus = entity.orderStatus,
                createdAt = entity.createdAt,
            )
        }
    }
}
