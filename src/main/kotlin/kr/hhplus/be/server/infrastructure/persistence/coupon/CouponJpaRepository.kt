package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {

    fun findWithLockById(id: Long): CouponEntity?
}