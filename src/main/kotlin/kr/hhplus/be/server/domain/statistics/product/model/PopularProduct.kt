package kr.hhplus.be.server.domain.statistics.product.model

import java.time.LocalDateTime

class PopularProduct(
    val id: Long = 0,
    val productId: Long,
    val rank: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
)