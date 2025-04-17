package kr.hhplus.be.server.infrastructure.persistence.statistics.model

interface PopularProductProjection {
    val id: Long

    val productId: Long

    val productName: String

    val rank: Int
}