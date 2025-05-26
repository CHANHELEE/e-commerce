package kr.hhplus.be.server.application.payment

import kr.hhplus.be.server.application.payment.model.PaymentTransactionalEvent
import kr.hhplus.be.server.infrastructure.api.payment.OrderDataPlatformClient
import kr.hhplus.be.server.infrastructure.api.payment.model.OrderDataPlatformDto
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentTransactionalEventListener(
    private val orderDataPlatformClient: OrderDataPlatformClient,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendToOrderDataPlatform(event: PaymentTransactionalEvent.TransactionCommitEvent) {
        orderDataPlatformClient.sendSuccessOrder(
            OrderDataPlatformDto.PaymentSuccess(
                paymentId = event.paymentId,
                userId = event.userId,
                productIdToQuantity = event.productIdToQuantity,
            )
        )
    }
}