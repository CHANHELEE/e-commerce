package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.fixtures.product.*
import org.assertj.core.api.Assertions
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
            given(productRepository.findBy(productCommand.productId)).willReturn(product)

            //when
            val returnedProductEntity = productService.getProductBy(productCommand)

            //then
            assertThat(returnedProductEntity)
                .extracting("id", "name", "price")
                .contains(product.id, product.name, product.price)
            verify(productRepository, times(1)).findBy(productCommand.productId)
        }

        @Test
        fun `존재하지 않는 상품 조회 시 Business 예외(PRODUCT_NOT_FOUND)가 발생한다 `() {

            //given
            val productCommand = ProductCommandFixture.get()
            val productEntity = null
            given(productRepository.findBy(productCommand.productId)).willReturn(productEntity)

            //when
            val exception = assertThrows<BusinessException> {
                productService.getProductBy(productCommand)
            }

            //then
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_NOT_FOUND)
            verify(productRepository, times(1)).findBy(productCommand.productId)

        }
    }

    @Nested
    inner class ProductOptions {

        @Test
        fun `상품옵션을 조회 한다`() {

            //given
            val productCommand = ProductOptionCommandFixture.get()
            val productOptions = listOf(
                ProductDetailViewFixture.get(1L),
                ProductDetailViewFixture.get(2L),
            )
            given(productRepository.findAllDetailsBy(productCommand.productId)).willReturn(productOptions)

            //when
            val returnedProduct = productService.getProductOptionsBy(productCommand)

            //then
            assertThat(returnedProduct)
                .hasSize(2)
                .extracting("productId", "name", "price", "size", "stock")
                .containsExactlyInAnyOrder(
                    Assertions.tuple(
                        productOptions[0].productId,
                        productOptions[0].name,
                        productOptions[0].price,
                        productOptions[0].size,
                        productOptions[0].stock,
                    ),
                    Assertions.tuple(
                        productOptions[1].productId,
                        productOptions[1].name,
                        productOptions[1].price,
                        productOptions[1].size,
                        productOptions[1].stock
                    )
                )
            verify(productRepository, times(1)).findAllDetailsBy(productCommand.productId)
        }

        @Test
        fun `상품 옵션이 존재하지 않을 시 Business 예외(PRODUCT_OPTIONS_NOT_FOUND)가 발생한다 `() {

            //given
            val productCommand = ProductOptionCommandFixture.get()
            val productEntity = null
            given(productRepository.findAllDetailsBy(productCommand.productId)).willReturn(productEntity)

            //when
            val exception = assertThrows<BusinessException> {
                productService.getProductOptionsBy(productCommand)
            }

            //then
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND)
            verify(productRepository, times(1)).findAllDetailsBy(productCommand.productId)

        }
    }
}