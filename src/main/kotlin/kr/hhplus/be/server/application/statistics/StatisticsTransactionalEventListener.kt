package kr.hhplus.be.server.application.statistics

import kr.hhplus.be.server.application.payment.model.PaymentTransactionalEvent
import kr.hhplus.be.server.domain.statistics.product.ProductStatisticService
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StatisticsTransactionalEventListener(
    private val productStatisticService: ProductStatisticService,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun increaseDailyPopularProduct(event: PaymentTransactionalEvent.TransactionCommitEvent) {
        productStatisticService.increaseDailyPopularProduct(PopularProductCommand.IncreaseDaily(event.productIdToQuantity))
    }
}