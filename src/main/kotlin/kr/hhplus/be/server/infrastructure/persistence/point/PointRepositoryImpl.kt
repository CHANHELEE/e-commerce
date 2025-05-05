package kr.hhplus.be.server.infrastructure.persistence.point


import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.domain.point.model.entity.Point
import kr.hhplus.be.server.domain.point.model.entity.PointHistory
import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointHistoryEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository,
    private val pointHistoryJpaRepository: PointHistoryJpaRepository,
) : PointRepository {

    override fun findUserPointWithLockBy(userId: Long): Point? {
        return pointJpaRepository.findWithLockByUserId(userId)?.toDomain()
    }

    override fun save(point: Point): Point {
        return pointJpaRepository.save(PointEntity.from(point)).toDomain()
    }

    override fun saveHistory(pointHistory: PointHistory): PointHistory {
        return pointHistoryJpaRepository.save(PointHistoryEntity.from(pointHistory)).toDomain()
    }

    override fun findUserPointBy(userId: Long): Point? {
        return pointJpaRepository.findByUserId(userId)?.toDomain()
    }
}