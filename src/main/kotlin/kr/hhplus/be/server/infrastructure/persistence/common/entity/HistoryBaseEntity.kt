package kr.hhplus.be.server.infrastructure.persistence.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class HistoryBaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
}