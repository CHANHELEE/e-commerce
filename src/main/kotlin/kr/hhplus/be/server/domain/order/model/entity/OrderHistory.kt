package kr.hhplus.be.server.domain.order.model.entity

import kr.hhplus.be.server.domain.order.enums.OrderStatus
import java.time.LocalDateTime

class OrderHistory(
    val id: Long = 0,
    val orderId: Long,
    var orderStatus: OrderStatus,
    var createdAt: LocalDateTime = LocalDateTime.now(),
)