package kr.hhplus.be.server.domain.order.model.entity

import kr.hhplus.be.server.domain.order.enums.OrderStatus
import java.time.LocalDateTime

class OrderHistory(
    val id: Long = 0,
    val orderId: Long,
    val orderStatus: OrderStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)