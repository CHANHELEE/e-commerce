package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.common.model.PagingResult
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductCommand
import kr.hhplus.be.server.domain.product.model.ProductDetailView
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {

    fun getProductBy(productCommand: ProductCommand.Product): Product =
        productRepository.findBy(productCommand.productId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)

    fun getProductOptionsBy(productCommand: ProductCommand.ProductOption): List<ProductDetailView> =
        productRepository.findAllDetailsBy(productCommand.productId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)

    fun getProductsBy(pageable: Pageable): PagingResult<Product> =
        productRepository.findAllBy(pageable)
            ?: throw BusinessException(BusinessErrorCode.PRODUCTS_NOT_EXIST)
}