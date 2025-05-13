package kr.hhplus.be.server.domain.product.model.entity

import java.time.LocalDateTime

class Product(
    val id: Long = 0,
    var name: String,
    var price: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)