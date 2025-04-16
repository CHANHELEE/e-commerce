package kr.hhplus.be.server.infrastructure.persistence.coupon.entity

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "users_coupons",
    uniqueConstraints = [UniqueConstraint(name = "uk_user_coupon", columnNames = ["user_id", "coupon_id"])]
)
class UserCouponEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "coupon_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_user_coupon_coupon")
    )
    val coupon: CouponEntity,

    @Column(name = "used_at", columnDefinition = "DATETIME(6)", nullable = true)
    var usedAt: LocalDateTime? = null

) : BaseEntity(
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)