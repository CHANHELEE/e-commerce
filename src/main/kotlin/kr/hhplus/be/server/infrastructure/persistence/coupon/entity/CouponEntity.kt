package kr.hhplus.be.server.infrastructure.persistence.coupon.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "coupons")
class CouponEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val amount: Int,

    @Column(name = "discount_price", nullable = false)
    val discountPrice: Int,

    @Column(nullable = false, length = 50)
    val name: String

) : BaseEntity(
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)