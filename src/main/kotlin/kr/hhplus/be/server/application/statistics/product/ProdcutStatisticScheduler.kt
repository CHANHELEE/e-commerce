package kr.hhplus.be.server.application.statistics.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.OrderRepository
import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.statistics.product.model.PopularProduct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class PopularProductScheduler(
    private val orderRepository: OrderRepository,
    private val productStatisticRepository: ProductStatisticRepository,
) {

    // 매일 12:00 실행 (정오)
    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    fun generatePopularProducts() {
        val start = LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1)
        val threeDaysAgo = start.minusDays(2)

        val topProducts = orderRepository.findTop5BestProduct(threeDaysAgo)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)

        if (topProducts.isEmpty()) {
            throw BusinessException(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)
        }

        val popularProducts = topProducts.mapIndexed { index, orderProduct ->
            PopularProduct(
                productId = orderProduct.productId,
                rank = index + 1
            )
        }
        productStatisticRepository.saveAllPopularProducts(popularProducts)
    }
}