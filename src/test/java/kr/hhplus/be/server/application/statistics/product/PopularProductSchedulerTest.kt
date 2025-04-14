package kr.hhplus.be.server.application.statistics.product


import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.OrderRepository
import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
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
    lateinit var orderRepository: OrderRepository

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
                OrderProduct(productId = 101L, productOptionId = 1L, orderId = 1L, productPrice = 1000L, quantity = 2L),
                OrderProduct(productId = 102L, productOptionId = 2L, orderId = 1L, productPrice = 2000L, quantity = 1L)
            )

            given(orderRepository.findTop5BestProduct(any())).willReturn(topProducts)
            given(productStatisticRepository.saveAllPopularProducts(any())).willReturn(true)

            // when & then: 예외 없으면 성공
            scheduler.generatePopularProducts()
        }

        @Test
        fun `인기 상품 조회 결과가 null이면 예외가 발생한다`() {
            // given
            given(orderRepository.findTop5BestProduct(any())).willReturn(null)

            // expect
            assertThatThrownBy {
                scheduler.generatePopularProducts()
            }.isInstanceOf(BusinessException::class.java)
                .hasMessageContaining(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND.message)
        }

        @Test
        fun `인기 상품이 비어있으면 예외가 발생한다`() {
            // given
            given(orderRepository.findTop5BestProduct(any())).willReturn(emptyList())

            // expect
            assertThatThrownBy {
                scheduler.generatePopularProducts()
            }.isInstanceOf(BusinessException::class.java)
                .hasMessageContaining(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND.message)
        }
    }
}