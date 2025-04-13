package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import java.time.LocalDateTime

class PointHistory(
    var id: Long? = null,
    var pointId: Long,
    var point: Long,
    var type: PointHistoryType,
    var createdAt: LocalDateTime? = null,
)