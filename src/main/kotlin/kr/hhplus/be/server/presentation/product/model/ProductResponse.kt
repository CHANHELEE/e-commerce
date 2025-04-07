package kr.hhplus.be.server.presentation.product.model

import java.time.LocalDateTime

class ProductResponse {

    data class Product(
        val id: Long,
        val name: String,
        val price: Long,
        val updatedAt: LocalDateTime,
    )

    data class ProductOption(
        val size: String,
        val stock: Long,
    )
}