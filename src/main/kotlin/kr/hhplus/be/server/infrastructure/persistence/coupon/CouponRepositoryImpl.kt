package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.domain.coupon.CouponRepository
import kr.hhplus.be.server.domain.coupon.model.UserCoupon
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl : CouponRepository {

    override fun findUserCouponWithLockBy(userId: Long, couponId: Long): UserCoupon? {
        TODO("Not yet implemented")
    }

    override fun findUserCouponBy(userId: Long, couponId: Long): UserCoupon? {
        TODO("Not yet implemented")
    }

    override fun saveUserCoupon(userCoupon: UserCoupon): UserCoupon {
        TODO("Not yet implemented")
    }
}