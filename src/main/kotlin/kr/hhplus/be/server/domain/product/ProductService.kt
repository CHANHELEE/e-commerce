package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.common.model.PagingResult
import kr.hhplus.be.server.domain.product.model.*
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

    fun getProductStockBy(productCommand: ProductCommand.ProductStock): ProductStock =
        productRepository.findStockBy(productCommand.productId, productCommand.optionId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)

    fun getProductStockWithLockBy(productCommand: ProductCommand.ProductStock): ProductStock =
        productRepository.findStockWithLockBy(productCommand.productId, productCommand.optionId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)

    fun updateStock(productCommand: ProductCommand.UpdateStock): ProductStock =
        productRepository.updateStock(UpdateProductStock(productCommand.stockId, productCommand.stock))
}