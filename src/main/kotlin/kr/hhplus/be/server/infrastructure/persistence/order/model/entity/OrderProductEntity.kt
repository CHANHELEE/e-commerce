package kr.hhplus.be.server.infrastructure.persistence.order.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
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

    @Column(name = "order_id", nullable = false)
    val orderId: Long,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "product_option_id", nullable = false)
    val productOptionId: Long,

    @Column(name = "product_price", nullable = false)
    val productPrice: Long,

    @Column(nullable = false)
    var amount: Long,

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)", nullable = true)
    var deletedAt: LocalDateTime? = null

) : BaseEntity() {

    fun toDomain(): OrderProduct {
        return OrderProduct(
            id = id,
            productOptionId = productOptionId,
            productId = productId,
            orderId = orderId,
            productPrice = productPrice,
            quantity = amount,
            deletedAt = deletedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun from(orderProducts: List<OrderProduct>): List<OrderProductEntity> {
            return orderProducts.map {
                OrderProductEntity(
                    id = it.id,
                    orderId = it.orderId,
                    productId = it.productId,
                    productOptionId = it.productOptionId,
                    productPrice = it.productPrice,
                    amount = it.quantity,
                    deletedAt = it.deletedAt
                )
            }

        }
    }
}