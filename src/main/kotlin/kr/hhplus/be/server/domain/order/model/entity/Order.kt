package kr.hhplus.be.server.domain.order.model.entity

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import java.time.LocalDateTime

class Order(
    val id: Long = 0,
    val userId: Long,
    var userCouponId: Long? = null,
    status: OrderStatus,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
) {

    var status: OrderStatus = status
        private set

    fun validateModifiable() {
        require(status != OrderStatus.SUCCESS) {
            throw BusinessException(BusinessErrorCode.ORDER_ALREADY_COMPLETED)
        }
    }

    fun modifyStatusTo(target: OrderStatus) {

        require(status != OrderStatus.SUCCESS) {
            throw BusinessException(BusinessErrorCode.ORDER_ALREADY_COMPLETED)
        }

        status = target
    }
}
