package kr.hhplus.be.server.infrastructure.persistence.product.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "product_stock",
    uniqueConstraints = [UniqueConstraint(name = "uk_product_stock", columnNames = ["product_id", "product_option_id"])]
)
class ProductStockEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_stock_product")
    )
    val product: ProductEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_option_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_stock_product_option")
    )
    val productOption: ProductOptionEntity,

    @Column(nullable = false)
    var stock: Long

) : BaseEntity(
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)