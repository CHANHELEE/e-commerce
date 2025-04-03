package kr.hhplus.be.server.presentation.user.model

class UserResponse {

    data class Point(
        val userId: Long,
        val point: Long,
    )

    data class Charge(
        val userId: Long,
        val point: Long,
    )
}