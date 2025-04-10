package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.common.model.PagingResult
import kr.hhplus.be.server.domain.product.model.ProductCommand
import kr.hhplus.be.server.domain.product.model.ProductStock
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
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.then
import org.springframework.data.domain.PageRequest

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

    @Nested
    inner class Products {

        @Test
        fun `상품목록을 조회 한다`() {

            //given
            val pageable = PageRequest.of(1, 10)
            val products = listOf(
                ProductFixture.get(1L),
                ProductFixture.get(2L),
            )
            val pagingResult = PagingResult(1, 1, 2, products)
            given(productRepository.findAllBy(pageable)).willReturn(pagingResult)

            //when
            val returnedProduct = productService.getProductsBy(pageable)

            //then
            assertThat(returnedProduct.currentPage).isEqualTo(pagingResult.currentPage)
            assertThat(returnedProduct.totalPages).isEqualTo(pagingResult.totalPages)
            assertThat(returnedProduct.totalElements).isEqualTo(pagingResult.totalElements)
            assertThat(returnedProduct.data)  // 상품 리스트에 대해 검증
                .extracting("id", "name", "price")
                .containsExactlyInAnyOrder(
                    Assertions.tuple(
                        products[0].id,
                        products[0].name,
                        products[0].price
                    ),
                    Assertions.tuple(
                        products[1].id,
                        products[1].name,
                        products[1].price
                    )
                )
            verify(productRepository, times(1)).findAllBy(pageable)
        }

        @Test
        fun `상품 목록이 존재하지 않을 시 Business 예외(PRODUCTS_NOT_EXIST)가 발생한다 `() {

            //given
            val pageable = PageRequest.of(1, 10)
            val pagingResult = null
            given(productRepository.findAllBy(pageable)).willReturn(pagingResult)

            //when
            val exception = assertThrows<BusinessException> {
                productService.getProductsBy(pageable)
            }

            //then
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCTS_NOT_EXIST)
            verify(productRepository, times(1)).findAllBy(pageable)

        }
    }

    @Nested
    inner class GetProductStockWithLockBy {

        @Test
        fun `재고가 존재하면 반환한다`() {
            // given
            val productCommand = ProductCommand.ProductStock(productId = 1L, optionId = 10L)
            val stock = ProductStock(id = 1L, productId = 1L, productOptionId = 10L, stock = 5L)

            given(productRepository.findStockWithLockBy(1L, 10L)).willReturn(stock)

            // when
            val result = productService.getProductStockWithLockBy(productCommand)

            // then
            assertThat(result).isEqualTo(stock)
        }

        @Test
        fun `재고가 존재하지 않으면 예외가 발생한다`() {
            // given
            val productCommand = ProductCommand.ProductStock(productId = 1L, optionId = 10L)
            given(productRepository.findStockWithLockBy(1L, 10L)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                productService.getProductStockWithLockBy(productCommand)
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.PRODUCT_NOT_FOUND)
        }
    }

    @Nested
    inner class UpdateStock {

        @Test
        fun `재고가 정상적으로 업데이트된다`() {
            // given
            val stockId = 1L
            val stockAmount = 50L
            val productCommand = ProductCommand.UpdateStock(stockId = stockId, stock = stockAmount)
            val updatedStock = ProductStock(id = stockId, productId = 1L, productOptionId = 10L, stock = stockAmount)

            given(productRepository.updateStock(any())).willReturn(updatedStock)

            // when
            val result = productService.updateStock(productCommand)

            // then
            then(productRepository).should().updateStock(check {
                assertThat(it.stockId).isEqualTo(stockId)
                assertThat(it.stock).isEqualTo(stockAmount)
            })

            assertThat(result).isEqualTo(updatedStock)
        }
    }
}