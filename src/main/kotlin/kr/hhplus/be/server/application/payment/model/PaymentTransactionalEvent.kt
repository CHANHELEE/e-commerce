package kr.hhplus.be.server.application.payment.model

import kr.hhplus.be.server.domain.order.enums.OrderStatus

class PaymentTransactionalEvent {

    data class TransactionRollBackEvent(
        val orderId: Long,
        val status: OrderStatus = OrderStatus.FAIL
    )

    data class TransactionCommitEvent(
        val productIdToQuantity: Map<Long, Long>
    )
}