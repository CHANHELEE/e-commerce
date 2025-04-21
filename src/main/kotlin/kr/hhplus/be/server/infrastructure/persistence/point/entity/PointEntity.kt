package kr.hhplus.be.server.infrastructure.persistence.point.entity

import jakarta.persistence.Entity
import jakarta.persistence.*
import kr.hhplus.be.server.domain.point.model.entity.Point
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity

@Entity
@Table(
    name = "points",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id"])]
)
class PointEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var point: Long

) : BaseEntity() {
    fun toDomain(): Point {
        return Point(
            id = id,
            userId = userId,
            point = point,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun from(point: Point): PointEntity {
            return PointEntity(
                id = point.id,
                userId = point.userId,
                point = point.point,
            )
        }
    }
}