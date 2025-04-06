package kr.hhplus.be.server.presentation.statistics.model

class StatisticResponse {

    data class TopFive(
        val productName: String,
        val rank: Int,
    )
}