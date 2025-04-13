package kr.hhplus.be.server.domain.product.model

data class ProductDetailView(
    var productId: Long,
    var name: String,
    var price: Long,
    var size: String,
    var stock: Long,
)
