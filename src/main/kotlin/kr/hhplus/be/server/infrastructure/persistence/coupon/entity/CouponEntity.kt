package kr.hhplus.be.server.infrastructure.persistence.coupon.entity

import jakarta.persistence.*
import kr.hhplus.be.server.domain.coupon.model.entity.Coupon
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(name = "coupons")
class CouponEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val amount: Long,

    @Column(name = "discount_price", nullable = false)
    val discountPrice: Long,

    @Column(nullable = false, length = 50)
    val name: String

) : BaseEntity()
{

    fun toDomain(): Coupon {
        return Coupon(
            id = id!!,
            amount = amount,
            discountPrice = discountPrice,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}