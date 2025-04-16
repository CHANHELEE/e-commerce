package kr.hhplus.be.server.infrastructure.persistence.point.entity

import jakarta.persistence.Entity
import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "points",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id"])]
)
class PointEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var point: Int

) : BaseEntity()