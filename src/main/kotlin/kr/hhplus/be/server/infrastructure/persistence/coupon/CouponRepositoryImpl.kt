package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.domain.coupon.CouponRepository
import kr.hhplus.be.server.domain.coupon.model.entity.Coupon
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.UserCouponEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : CouponRepository {

    override fun findUserCouponWithLockBy(userCouponId: Long): UserCoupon? {
        return userCouponJpaRepository.findWithLockById(userCouponId)?.toDomain()
    }

    override fun findUserCouponBy(userId: Long, couponId: Long): UserCoupon? {
        return userCouponJpaRepository.findByUserIdAndCouponId(userId, couponId)?.toDomain()
    }

    override fun saveUserCoupon(userCoupon: UserCoupon): UserCoupon {

        val coupon = couponJpaRepository.getReferenceById(userCoupon.couponId)

        val userCoupon = userCouponJpaRepository.save(
            UserCouponEntity(
                id = userCoupon.id,
                userId = userCoupon.userId,
                coupon = coupon,
                usedAt = userCoupon.usedAt
            )
        ).toDomain()
        return userCoupon
    }

    override fun findCouponBy(couponId: Long): Coupon? {
        return couponJpaRepository.findByIdOrNull(couponId)?.toDomain()
    }

    override fun findCouponWithLockBy(couponId: Long): Coupon? {
        return couponJpaRepository.findWithLockById(couponId)?.toDomain()
    }

    override fun saveCoupon(coupon: Coupon): Coupon {

        return couponJpaRepository.save(
            CouponEntity(
                id = coupon.id,
                amount = coupon.amount,
                discountPrice = coupon.discountPrice,
                name = coupon.name
            )
        ).toDomain()
    }
}