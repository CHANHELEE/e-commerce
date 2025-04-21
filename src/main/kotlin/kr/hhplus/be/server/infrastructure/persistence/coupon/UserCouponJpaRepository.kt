package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.UserCouponEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, Long> {

    @EntityGraph(attributePaths = ["coupon"])
    fun findByUserIdAndCouponId(userId: Long, couponId: Long): UserCouponEntity?

    fun findWithLockById(userCouponId: Long): UserCouponEntity?
}