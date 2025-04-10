package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import java.time.LocalDateTime

class Point(
    var id: Long? = null,
    var userId: Long,
    point: Long,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {

    var point: Long = point
        private set

    companion object {
        const val MIN = 1L
        const val MAX = 10_000_000L
    }

    fun charge(amount: Long) {

        require(amount >= MIN) {
            throw BusinessException(BusinessErrorCode.INVALID_POINT_CHARGE_AMOUNT)

        }

        val chargedPoint = point + amount
        require(chargedPoint <= MAX) {
            throw BusinessException(BusinessErrorCode.EXCEED_POINT_LIMIT)
        }

        point = chargedPoint
    }

    fun validateUsable() {

        require(point > 0) {
            throw BusinessException(BusinessErrorCode.POINT_NOT_ENOUGH)
        }
    }

    fun use(amount: Long) {

        point -= amount
        require(point >= 0) {
            throw BusinessException(BusinessErrorCode.POINT_NOT_ENOUGH)
        }
    }
}