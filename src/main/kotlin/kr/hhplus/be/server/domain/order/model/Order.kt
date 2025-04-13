package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.order.enums.OrderStatus
import java.time.LocalDateTime

class Order(
    val id: Long = 0,
    val userId: Long,
    val userCouponId: Long? = null,
    val status: OrderStatus,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)