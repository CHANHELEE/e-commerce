package kr.hhplus.be.server.fixtures.point

import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import kr.hhplus.be.server.domain.point.model.Point
import kr.hhplus.be.server.domain.point.model.PointHistory
import java.time.LocalDateTime

object PointFixture {

    fun get(
        id: Long = 1L,
        userId: Long = 1L,
        point: Long = 10_000L,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now(),
    ): Point = Point(
        id = id,
        userId = userId,
        point = point,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

object PointHistoryFixture {

    fun get(
        id: Long = 1L,
        pointId: Long = 1L,
        point: Long = 10_000L,
        type: PointHistoryType = PointHistoryType.CHARGE,
        createdAt: LocalDateTime = LocalDateTime.now(),
    ): PointHistory = PointHistory(
        id = id,
        pointId = pointId,
        point = point,
        type = type,
        createdAt = createdAt,
    )
}