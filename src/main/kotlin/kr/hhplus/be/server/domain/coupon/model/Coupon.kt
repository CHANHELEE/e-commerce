package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import java.time.LocalDateTime

class Coupon(
    val id: Long = 0,
    var amount: Long,
    val discountPrice: Long,
    val name: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {

    fun issue() {
        require(amount > 0) {
            throw BusinessException(BusinessErrorCode.COUPON_OUT_OF_AMOUNT)
        }
        amount -= 1L
    }
}