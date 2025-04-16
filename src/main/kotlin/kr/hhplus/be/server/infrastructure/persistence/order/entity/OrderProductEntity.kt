package kr.hhplus.be.server.infrastructure.persistence.order.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "orders_products",
    uniqueConstraints = [UniqueConstraint(
        name = "uk_order_product",
        columnNames = ["order_id", "product_id", "product_option_id"]
    )]
)
class OrderProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_order_product_order")
    )
    val order: OrderEntity,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "product_option_id", nullable = false)
    val productOptionId: Long,

    @Column(name = "product_price", nullable = false)
    val productPrice: Int,

    @Column(nullable = false)
    var amount: Int,

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)", nullable = true)
    var deletedAt: LocalDateTime? = null

) : BaseEntity()