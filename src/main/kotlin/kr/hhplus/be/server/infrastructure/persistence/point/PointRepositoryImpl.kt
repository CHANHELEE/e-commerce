package kr.hhplus.be.server.infrastructure.persistence.point


import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.domain.point.model.entity.Point
import kr.hhplus.be.server.domain.point.model.entity.PointHistory
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository,
    private val pointHistoryJpaRepository: PointHistoryJpaRepository,
) : PointRepository {

    override fun findUserPointWithLockBy(userId: Long): Point? {
        TODO("Not yet implemented")
//        return pointJpaRepository.fin
    }

    override fun savePoint(point: Point): Point {
        TODO("Not yet implemented")
    }

    override fun savePointHistory(pointHistory: PointHistory): PointHistory {
        TODO("Not yet implemented")
    }

    override fun findUserPointBy(userId: Long): Point? {
        TODO("Not yet implemented")
    }
}