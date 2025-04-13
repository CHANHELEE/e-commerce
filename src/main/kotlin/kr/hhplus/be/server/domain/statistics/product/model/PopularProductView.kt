package kr.hhplus.be.server.domain.statistics.product.model

data class PopularProductView(
    val id: Long = 0,
    val productName: String,
    val rank: Int,
)
