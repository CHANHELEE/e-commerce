package kr.hhplus.be.server.infrastructure.persistence.coupon

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(id: Long): CouponEntity?
}