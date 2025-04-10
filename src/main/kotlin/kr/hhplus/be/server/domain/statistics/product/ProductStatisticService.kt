package kr.hhplus.be.server.domain.statistics.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductView
import org.springframework.stereotype.Service

@Service
class ProductStatisticService(
    private val productStatisticRepository: ProductStatisticRepository
) {

    fun getAllPopularProducts(): List<PopularProductView> =
        productStatisticRepository.findAllPopularProduct()
            ?: throw BusinessException(BusinessErrorCode.POPULAR_PRODUCTS_NOT_EXIST)

}