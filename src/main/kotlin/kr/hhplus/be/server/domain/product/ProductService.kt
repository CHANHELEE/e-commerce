package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.product.model.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {

    fun getBy(productCommand: ProductCommand.Product): ProductView =
        ProductView.from(
            productRepository.findBy(productCommand.productId)
                ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)
        )

    fun getDetailsBy(productCommand: ProductCommand.Detail): List<ProductDetailView> =
        productRepository.findAllDetailsBy(productCommand.productId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)


    fun validateStock(productCommand: ProductCommand.ProductStock): ProductStockView {

        val stock = productRepository.findStockBy(productCommand.productId, productCommand.optionId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)
        stock.validateStock()
        return ProductStockView.from(stock)
    }

    @Transactional
    fun decreaseStock(productCommand: ProductCommand.UpdateStock): ProductStockView {

        val stock = productRepository.findStockWithLockBy(productCommand.productId, productCommand.optionId)
            ?: throw BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND)
        stock.decreaseStock(productCommand.amount)
        return ProductStockView.from(productRepository.saveStock(stock))
    }
}