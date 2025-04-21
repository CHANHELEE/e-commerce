package kr.hhplus.be.server.infrastructure.persistence.product.model

interface ProductDetailProjection {
    val productId: Long
    val name: String
    val price: Long
    val size: String
    val stock: Long
}