package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.domain.point.model.entity.Point
import java.time.LocalDateTime

data class PointView(
    val id: Long? = null,
    var userId: Long,
    var point: Long,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: Point): PointView {
            return PointView(
                id = entity.id,
                userId = entity.userId,
                point = entity.point,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}
