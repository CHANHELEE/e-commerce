package kr.hhplus.be.server.domain.product.model

import kr.hhplus.be.server.domain.product.model.entity.Product
import kr.hhplus.be.server.domain.product.model.entity.ProductStock
import java.time.LocalDateTime

data class ProductStockView(
    val id: Long,
    val productId: Long,
    val productOptionId: Long,
    val stock: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: ProductStock): ProductStockView {
            return ProductStockView(
                id = entity.id,
                productId = entity.productId,
                productOptionId = entity.productOptionId,
                stock = entity.stock,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}

data class ProductView(
    val id: Long,
    val name: String,
    val price: Long,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(entity: Product): ProductView {
            return ProductView(
                id = entity.id,
                name = entity.name,
                price = entity.price,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt!!,
            )
        }
    }
}

data class ProductDetailView(
    val productId: Long,
    val name: String,
    val price: Long,
    val size: String,
    val stock: Long,
)
