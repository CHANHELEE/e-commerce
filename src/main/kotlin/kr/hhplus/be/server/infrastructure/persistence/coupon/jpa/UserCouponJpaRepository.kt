package kr.hhplus.be.server.infrastructure.persistence.coupon.jpa

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.UserCouponEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, Long> {

    @EntityGraph(attributePaths = ["coupon"])
    fun findByUserIdAndCouponId(userId: Long, couponId: Long): UserCouponEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(userCouponId: Long): UserCouponEntity?
}