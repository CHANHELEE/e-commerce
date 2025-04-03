package kr.hhplus.be.server.presentation.statistic.model

class StatisticResponse {

    data class TopFive(
        val productName: String,
        val rank: Int,
    )
}