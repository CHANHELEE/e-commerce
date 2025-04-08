package kr.hhplus.be.server.domain.product.model

import java.time.LocalDateTime

class Product(
    var id: Long? = null,
    name: String,
    price: Long,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    var name: String = name
        private set

    var price: Long = price
        private set
}