package kr.hhplus.be.server.domain.order.model.entity

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import java.time.LocalDateTime

class Order(
    val id: Long = 0,
    val userId: Long,
    val userCouponId: Long? = null,
    val status: OrderStatus,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
) {
    fun validateModifiable() {
        require(status != OrderStatus.SUCCESS) {
            throw BusinessException(BusinessErrorCode.ORDER_ALREADY_COMPLETED)
        }
    }
}
