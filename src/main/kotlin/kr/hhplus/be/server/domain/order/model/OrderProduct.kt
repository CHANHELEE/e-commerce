package kr.hhplus.be.server.domain.order.model


import java.time.LocalDateTime

class OrderProduct(
    val id: Long = 0,
    val productOptionId: Long,
    val orderId: Long,
    val productPrice: Long,
    var quantity: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)