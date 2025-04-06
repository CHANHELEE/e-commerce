package kr.hhplus.be.server.presentation.product.model

class ProductResponse {

    data class Product(
        val id: Long,
        val name: String,
        val price: Long,
        val stocks: Long,
        val size: String,
    )
}