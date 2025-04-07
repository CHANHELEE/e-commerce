package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductCommand
import kr.hhplus.be.server.domain.product.model.ProductOption
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {

    fun getProductBy(productCommand: ProductCommand.Product): Product =
        productRepository.findById(productCommand.productId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)

    fun getProductOptionsBy(productCommand: ProductCommand.ProductOption): List<ProductOption> =
        productRepository.findAllOptionsBy(productCommand.productId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)

}