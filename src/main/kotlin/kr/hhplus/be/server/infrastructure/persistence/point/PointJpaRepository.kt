package kr.hhplus.be.server.infrastructure.persistence.point

import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointJpaRepository : JpaRepository<PointEntity, Long> {

    fun findWithLockByUserId(userId: Long): PointEntity?

    fun findByUserId(userId: Long): PointEntity?
}