package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.point.model.Point
import kr.hhplus.be.server.domain.point.model.PointHistory

interface PointRepository {

    fun findUserPointWithLockBy(userId: Long): Point?

    fun savePoint(point: Point): Point

    fun savePointHistory(pointHistory: PointHistory): PointHistory
}