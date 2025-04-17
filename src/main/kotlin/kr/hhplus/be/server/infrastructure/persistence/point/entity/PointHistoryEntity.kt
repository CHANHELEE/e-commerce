package kr.hhplus.be.server.infrastructure.persistence.point.entity

import jakarta.persistence.Entity
import jakarta.persistence.*
import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import kr.hhplus.be.server.domain.point.model.entity.Point
import kr.hhplus.be.server.domain.point.model.entity.PointHistory
import kr.hhplus.be.server.infrastructure.persistence.common.entity.HistoryBaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "points_histories")
class PointHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val pointId: Long,

    @Column(nullable = false)
    val pointAmount: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    val type: PointHistoryType,
) : HistoryBaseEntity() {

    fun toDomain(): PointHistory {
        return PointHistory(
            id = id,
            pointId = pointId,
            point = pointAmount,
            type = type,
            createdAt = createdAt
        )
    }

    companion object {
        fun from(pointHistory: PointHistory): PointHistoryEntity {
            return PointHistoryEntity(
                id = pointHistory.id,
                pointId = pointHistory.pointId,
                pointAmount = pointHistory.point,
                type = pointHistory.type,
            )
        }
    }

}