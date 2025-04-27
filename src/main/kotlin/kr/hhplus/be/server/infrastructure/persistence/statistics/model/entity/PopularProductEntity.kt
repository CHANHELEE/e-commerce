package kr.hhplus.be.server.infrastructure.persistence.statistics.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(
    name = "popular_product",
    uniqueConstraints = [UniqueConstraint(name = "uk_popular_product", columnNames = ["product_id"])]
)
class PopularProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false)
    val ranking: Int

) : BaseEntity() {

    fun toDomain(): PopularProduct {
        return PopularProduct(
            id = id,
            productId = productId,
            name = name,
            ranking = ranking,
            createdAt = createdAt
        )
    }

    companion object {
        fun from(popularProduct: PopularProduct): PopularProductEntity {
            return PopularProductEntity(
                id = popularProduct.id,
                productId = popularProduct.productId,
                name = popularProduct.name,
                ranking = popularProduct.ranking,
            )
        }
    }
}