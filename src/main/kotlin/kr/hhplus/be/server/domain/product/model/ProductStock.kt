package kr.hhplus.be.server.domain.product.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import java.time.LocalDateTime

class ProductStock(
    val id: Long = 0,
    val productId: Long = 0,
    val productOptionId: Long = 0,
    var stock: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {

    fun validateStockForOrder() {

        require(stock > 0) {
            throw BusinessException(BusinessErrorCode.PRODUCT_STOCK_OUT_OF_STOCK)
        }
    }
}