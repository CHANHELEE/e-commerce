package kr.hhplus.be.server.domain.statistics.product.model

class PopularProductCommand {

    data class IncreaseDaily(
        val productIdToQuantity: Map<Long, Long>
    )
}