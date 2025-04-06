package kr.hhplus.be.server.presentation.user.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class UserResponse {

    data class Point(
        val userId: Long,
        val point: Long,
    )

    data class Charge(
        val userId: Long,
        val point: Long,
    )

    data class Coupons(
        val userId: Long,
        val couponId: Long,
        val couponName: String,
        @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val usedAt: LocalDateTime? = null,
    )
}