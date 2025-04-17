package kr.hhplus.be.server.application.statistics.product


import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductView
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given

@ExtendWith(MockitoExtension::class)
class PopularProductSchedulerTest {

    @Mock
    lateinit var productStatisticRepository: ProductStatisticRepository

    @InjectMocks
    lateinit var scheduler: PopularProductScheduler

    @Nested
    inner class GeneratePopularProducts {

        @Test
        fun `정상적으로 인기 상품이 저장된다`() {
            // given
            val topProducts = listOf(
                PopularProductView(id = 1, productId = 1L, productName = "test1", rank = 1),
                PopularProductView(id = 2, productId = 2L, productName = "test1", rank = 2),
                PopularProductView(id = 3, productId = 3L, productName = "test1", rank = 3),
                PopularProductView(id = 4, productId = 4L, productName = "test1", rank = 4),
                PopularProductView(id = 5, productId = 5L, productName = "test1", rank = 5),
            )
            given(productStatisticRepository.findTop5BestProduct(any())).willReturn(topProducts)
            given(productStatisticRepository.saveAllPopularProducts(any())).willReturn(true)

            // when & then: 예외 없으면 성공
            scheduler.generatePopularProducts()
        }

        @Test
        fun `인기 상품 조회 결과가 null이면 예외가 발생한다`() {
            // given
            given(productStatisticRepository.findTop5BestProduct(any())).willReturn(null)

            // expect
            assertThatThrownBy {
                scheduler.generatePopularProducts()
            }.isInstanceOf(BusinessException::class.java)
                .hasMessageContaining(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND.message)
        }

        @Test
        fun `인기 상품이 비어있으면 예외가 발생한다`() {
            // given
            given(productStatisticRepository.findTop5BestProduct(any())).willReturn(emptyList())

            // expect
            assertThatThrownBy {
                scheduler.generatePopularProducts()
            }.isInstanceOf(BusinessException::class.java)
                .hasMessageContaining(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND.message)
        }
    }
}