package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.fixtures.product.ProductCommandFixture
import kr.hhplus.be.server.fixtures.product.ProductFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @InjectMocks
    private lateinit var productService: ProductService

    @Mock
    private lateinit var productRepository: ProductRepository

    @Nested
    inner class Product {

        @Test
        fun `상품을 조회 한다`() {

            //given
            val productCommand = ProductCommandFixture.get()
            val product = ProductFixture.get()
            given(productRepository.findById(productCommand.productId)).willReturn(product)

            //when
            val returnedProductEntity = productService.getProductBy(productCommand)

            //then
            assertThat(returnedProductEntity)
                .extracting("id", "name", "price")
                .contains(product.id, product.name, product.price)
            verify(productRepository, times(1)).findById(productCommand.productId)
        }

        @Test
        fun `존재하지 않는 상품 조회 시 Business 예외(PRODUCT_NOT_FOUND)가 발생한다 `() {

            //given
            val productCommand = ProductCommandFixture.get()
            val productEntity = null
            given(productRepository.findById(productCommand.productId)).willReturn(productEntity)

            //when
            val exception = assertThrows<BusinessException> {
                productService.getProductBy(productCommand)
            }

            //then
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_NOT_FOUND)
            verify(productRepository, times(1)).findById(productCommand.productId)

        }
    }
}