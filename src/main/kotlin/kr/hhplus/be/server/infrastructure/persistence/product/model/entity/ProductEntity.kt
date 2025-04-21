package kr.hhplus.be.server.infrastructure.persistence.product.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.product.model.entity.Product
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(name = "products")
class ProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(nullable = false)
    var price: Long,

) : BaseEntity() {

    fun toDomain(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}