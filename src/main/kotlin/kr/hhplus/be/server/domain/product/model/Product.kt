package kr.hhplus.be.server.domain.product.model

import java.time.LocalDateTime

data class Product(
    val id: Long,
    var name: String,
    var price: Long,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)
