package kr.hhplus.be.server.infrastructure.persistence.order.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "user_coupon_id", nullable = true)
    var userCouponId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: OrderStatus,

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)", nullable = true)
    var deletedAt: LocalDateTime? = null

) : BaseEntity()