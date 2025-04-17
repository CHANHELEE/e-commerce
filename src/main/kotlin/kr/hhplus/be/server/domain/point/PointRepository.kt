package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.point.model.entity.Point
import kr.hhplus.be.server.domain.point.model.entity.PointHistory

interface PointRepository {

    fun findUserPointWithLockBy(userId: Long): Point?

    fun savePoint(point: Point): Point

    fun savePointHistory(pointHistory: PointHistory): PointHistory

    fun findUserPointBy(userId: Long): Point?
}