package kr.hhplus.be.server.domain.point.model.entity

import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import java.time.LocalDateTime

class PointHistory(
    val id: Long = 0,
    val pointId: Long,
    val point: Long,
    val type: PointHistoryType,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)