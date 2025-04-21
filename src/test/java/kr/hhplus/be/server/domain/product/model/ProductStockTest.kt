package kr.hhplus.be.server.domain.product.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.product.model.entity.ProductStock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
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
            stock.validateStock()
        }

        //then
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_STOCK_OUT_OF_STOCK)
    }

    @Nested
    inner class DecreaseStock {

        @Test
        fun `재고가 충분하면 재고가 정상 차감된다`() {
            // given
            val stock = ProductStock(
                id = 1L,
                productId = 100L,
                productOptionId = 200L,
                stock = 10
            )

            // when
            stock.decreaseStock(3)

            // then
            assertThat(stock.stock).isEqualTo(7)
        }

        @Test
        fun `차감 후 재고가 0 이하면 예외가 발생한다`() {
            // given
            val stock = ProductStock(
                id = 1L,
                productId = 100L,
                productOptionId = 200L,
                stock = 2
            )

            // when & then
            val exception = assertThrows<BusinessException> {
                stock.decreaseStock(3)
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_STOCK_OUT_OF_STOCK)
        }
    }

}