package kr.hhplus.be.server.domain.product.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductStockTest {

    @Test
    fun `재고가 0이면 PRODUCT_STOCK_OUT_OF_STOCK 예외가 발생한다`() {
        val stock = ProductStock(
            id = 1L,
            productId = 100L,
            productOptionId = 200L,
            stock = 0L
        )

        //when
        val exception = assertThrows<BusinessException> {
            stock.validateStockForOrder()
        }

        //then
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_STOCK_OUT_OF_STOCK)
    }

}