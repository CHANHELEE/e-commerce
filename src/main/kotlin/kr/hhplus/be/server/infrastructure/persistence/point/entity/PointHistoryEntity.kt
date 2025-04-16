package kr.hhplus.be.server.infrastructure.persistence.point.entity

import jakarta.persistence.Entity
import jakarta.persistence.*
import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import kr.hhplus.be.server.infrastructure.persistence.common.entity.HistoryBaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "points_histories")
class PointHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "point_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_point_history_point")
    )
    val point: PointEntity,

    @Column(nullable = false)
    val pointAmount: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    val type: PointHistoryType,
) : HistoryBaseEntity()