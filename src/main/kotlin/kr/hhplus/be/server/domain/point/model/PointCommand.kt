package kr.hhplus.be.server.domain.point.model

class PointCommand {

    data class Charge(
        val userId: Long,
        val amount: Long,
    )
}