package kr.hhplus.be.server.domain.coupon.model.entity

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import java.time.LocalDateTime

class UserCoupon(
    val id: Long = 0,
    val couponId: Long,
    val userId: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var usedAt: LocalDateTime? = null,
) {


    fun validateUsable() {

        require(usedAt == null) {
            throw BusinessException(BusinessErrorCode.USER_COUPON_ALREADY_USED)
        }
    }

    fun use() {

        require(usedAt == null) {
            throw BusinessException(BusinessErrorCode.USER_COUPON_ALREADY_USED)
        }
        usedAt = LocalDateTime.now()
    }
}