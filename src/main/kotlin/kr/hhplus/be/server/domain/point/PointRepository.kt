package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.point.model.entity.Point
import kr.hhplus.be.server.domain.point.model.entity.PointHistory

interface PointRepository {

    fun findUserPointWithLockBy(userId: Long): Point?

    fun save(point: Point): Point

    fun saveHistory(pointHistory: PointHistory): PointHistory

    fun findUserPointBy(userId: Long): Point?
}