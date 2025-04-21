package kr.hhplus.be.server.presentation.point.model

class PointResponse {

    data class Point(
        val userId: Long,
        val point: Long,
    )

    data class Charge(
        val userId: Long,
        val point: Long,
    )
}