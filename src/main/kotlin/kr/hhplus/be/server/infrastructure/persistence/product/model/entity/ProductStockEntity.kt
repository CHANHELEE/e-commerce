package kr.hhplus.be.server.infrastructure.persistence.product.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.product.model.entity.ProductStock
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(
    name = "product_stock",
    uniqueConstraints = [UniqueConstraint(name = "uk_product_stock", columnNames = ["product_id", "product_option_id"])]
)
class ProductStockEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false)
    val productOptionId: Long,

    @Column(nullable = false)
    var stock: Long

) : BaseEntity() {

    fun toDomain(): ProductStock {
        return ProductStock(
            id = id,
            productId = productId,
            productOptionId = productOptionId,
            stock = stock,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    companion object {
        fun from(productStock: ProductStock): ProductStockEntity {
            return ProductStockEntity(
                id = productStock.id,
                productId = productStock.productId,
                productOptionId = productStock.productOptionId,
                stock = productStock.stock,
            )
        }
    }
}