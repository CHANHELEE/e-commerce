package kr.hhplus.be.server.presentation.point.model

class PointRequest {

    data class Charge(
        val userId: Long,
        val point: Long,
    )
}