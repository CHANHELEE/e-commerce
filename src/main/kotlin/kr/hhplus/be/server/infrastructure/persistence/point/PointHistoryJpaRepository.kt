package kr.hhplus.be.server.infrastructure.persistence.point

import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointHistoryJpaRepository: JpaRepository<PointHistoryEntity, Long> {
}