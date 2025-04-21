package kr.hhplus.be.server.presentation.statistics.model

class StatisticResponse {

    data class TopFive(
        val productId: Long,
        val productName: String,
        val rank: Int,
    )
}