package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.domain.coupon.model.entity.Coupon
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import java.time.LocalDateTime


data class UserCouponView(
    val id: Long = 0,
    var couponId: Long,
    var userId: Long,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime?,
    var usedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: UserCoupon): UserCouponView {
            return UserCouponView(
                id = entity.id,
                couponId = entity.couponId,
                userId = entity.userId,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                usedAt = entity.usedAt
            )
        }
    }
}

data class CouponView(
    val id: Long = 0,
    var amount: Long,
    var discountPrice: Long,
    var name: String,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: Coupon): CouponView {
            return CouponView(
                id = entity.id,
                amount = entity.amount,
                discountPrice = entity.discountPrice,
                name = entity.name,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}