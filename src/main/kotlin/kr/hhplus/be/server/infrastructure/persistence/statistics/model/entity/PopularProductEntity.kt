package kr.hhplus.be.server.infrastructure.persistence.statistics.model.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

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

) : BaseEntity()