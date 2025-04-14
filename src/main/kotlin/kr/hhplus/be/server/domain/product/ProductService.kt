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

    fun getProductBy(productCommand: ProductCommand.Product): ProductView =
        ProductView.from(
            productRepository.findBy(productCommand.productId)
                ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)
        )

    fun getProductDetailsBy(productCommand: ProductCommand.ProductOption): List<ProductDetailView> =
        productRepository.findAllDetailsBy(productCommand.productId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)

    fun getProductsBy(pageable: Pageable): PagingResult<ProductView> {
        val result = productRepository.findAllBy(pageable)
            ?: throw BusinessException(BusinessErrorCode.PRODUCTS_NOT_EXIST)

        val convertedContent = result.data.map { ProductView.from(it) }

        return PagingResult(
            data = convertedContent,
            totalPages = result.totalPages,
            totalElements = result.totalElements,
            currentPage = result.currentPage,
        )
    }

    fun validateStock(productCommand: ProductCommand.ProductStock): ProductStockView {

        val stock = productRepository.findStockBy(productCommand.productId, productCommand.optionId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)
        stock.validateStock()
        return ProductStockView.from(stock)
    }

    fun decreaseStock(productCommand: ProductCommand.UpdateStock): ProductStockView {

        val stock = productRepository.findStockWithLockBy(productCommand.productId, productCommand.optionId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)
        stock.decreaseStock(productCommand.amount)
        return ProductStockView.from(productRepository.updateStock(stock))
    }
}