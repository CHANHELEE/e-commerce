package kr.hhplus.be.server.infrastructure.persistence.product.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "products_options",
    uniqueConstraints = [UniqueConstraint(name = "uk_product_option", columnNames = ["product_id", "size"])]
)
class ProductOptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_option_product")
    )
    val product: ProductEntity,

    @Column(nullable = false, length = 30)
    var size: String,

    @Column(nullable = false)
    var stock: Long

) : BaseEntity()