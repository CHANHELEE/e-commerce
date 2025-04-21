package kr.hhplus.be.server.domain.statistics.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductView
import org.springframework.stereotype.Service

@Service
class ProductStatisticService(
    private val productStatisticRepository: ProductStatisticRepository
) {

    fun getAllPopularProducts(): List<PopularProductView> {
        val popularProducts = productStatisticRepository.findAllPopularProducts()
            ?: throw BusinessException(BusinessErrorCode.POPULAR_PRODUCTS_NOT_EXIST)

        return popularProducts.map { PopularProductView(
            id = it.id,
            productId = it.productId,
            productName = it.name,
            rank = it.ranking
        ) }
    }
}