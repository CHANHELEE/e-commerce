package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.domain.coupon.CouponRepository
import kr.hhplus.be.server.domain.coupon.model.entity.Coupon
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl : CouponRepository {

    override fun findUserCouponWithLockBy(userCouponId: Long): UserCoupon? {
        TODO("Not yet implemented")
    }

    override fun findUserCouponBy(userId: Long, couponId: Long): UserCoupon? {
        TODO("Not yet implemented")
    }

    override fun saveUserCoupon(userCoupon: UserCoupon): UserCoupon {
        TODO("Not yet implemented")
    }

    override fun updateUserCoupon(userCoupon: UserCoupon): UserCoupon {
        TODO("Not yet implemented")
    }

    override fun findCouponBy(couponId: Long): Coupon? {
        TODO("Not yet implemented")
    }

    override fun findCouponWithLockBy(userId: Long): Coupon? {
        TODO("Not yet implemented")
    }
}