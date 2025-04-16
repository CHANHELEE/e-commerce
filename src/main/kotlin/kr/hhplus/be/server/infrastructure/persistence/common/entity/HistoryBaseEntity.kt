package kr.hhplus.be.server.infrastructure.persistence.common.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class HistoryBaseEntity(
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
    val createdAt: LocalDateTime
)