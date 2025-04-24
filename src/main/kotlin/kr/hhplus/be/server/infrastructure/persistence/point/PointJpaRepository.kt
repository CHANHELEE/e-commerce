package kr.hhplus.be.server.infrastructure.persistence.point

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface PointJpaRepository : JpaRepository<PointEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockByUserId(userId: Long): PointEntity?

    fun findByUserId(userId: Long): PointEntity?
}