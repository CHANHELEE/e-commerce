package kr.hhplus.be.server.domain.statistics.product.model

data class PopularProductView(
    val id: Long = 0,
    val productId: Long,
    val productName: String,
    val rank: Int,
)


data class PopularProductAggregateView(
    val productId: Long,
    val productName: String,
)