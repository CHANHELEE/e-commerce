package kr.hhplus.be.server.infrastructure.persistence.user.entity

import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.persistence.common.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 10)
    var name: String

) : BaseEntity()