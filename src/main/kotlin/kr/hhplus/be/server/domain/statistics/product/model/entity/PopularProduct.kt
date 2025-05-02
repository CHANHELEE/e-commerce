package kr.hhplus.be.server.domain.statistics.product.model.entity

import java.time.LocalDateTime

class PopularProduct(
    val id: Long = 0,
    val productId: Long,
    var name: String,
    val ranking: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
)