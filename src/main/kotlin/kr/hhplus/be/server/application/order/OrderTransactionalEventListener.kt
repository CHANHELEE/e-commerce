package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.payment.model.PaymentTransactionalEvent
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.model.OrderCommand
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderTransactionalEventListener(
    private val orderService: OrderService,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun modifyOrderStatus(event: PaymentTransactionalEvent.TransactionRollBackEvent) {
        orderService.modifyStatus(
            OrderCommand.ModifyStatus(
                event.orderId,
                event.status,
            )
        )
    }
}