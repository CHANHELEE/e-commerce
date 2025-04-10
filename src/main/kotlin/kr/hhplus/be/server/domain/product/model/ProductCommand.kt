package kr.hhplus.be.server.domain.product.model

class ProductCommand {

    data class Product(
        val productId: Long,
    )

    data class ProductStock(
        val productId: Long,
        val optionId: Long,
    )

    data class ProductOption(
        val productId: Long,
    )
}