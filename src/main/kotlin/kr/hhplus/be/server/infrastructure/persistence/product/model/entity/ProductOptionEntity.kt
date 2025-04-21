package kr.hhplus.be.server.infrastructure.persistence.product.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(
    name = "products_options",
    uniqueConstraints = [UniqueConstraint(name = "uk_product_option", columnNames = ["product_id", "size"])]
)
class ProductOptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false, length = 30)
    var size: String,

    @Column(nullable = false)
    var stock: Long

) : BaseEntity()