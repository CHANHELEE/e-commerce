package kr.hhplus.be.server.application.statistics.product


import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductAggregateView
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever
import org.springframework.data.redis.core.StringRedisTemplate

@ExtendWith(MockitoExtension::class)
class PopularProductSchedulerTest {

    @Mock
    lateinit var productStatisticRepository: ProductStatisticRepository

    @Mock
    lateinit var redisTemplate: StringRedisTemplate

    @InjectMocks
    lateinit var scheduler: PopularProductScheduler

    @Nested
    inner class GeneratePopularProducts {

        @Test
        fun `정상적으로 인기 상품이 저장된다`() {
            // given
            val topProducts = listOf(
                PopularProductAggregateView(productId = 1L, productName = "test1"),
                PopularProductAggregateView(productId = 2L, productName = "test1"),
                PopularProductAggregateView(productId = 3L, productName = "test1"),
                PopularProductAggregateView(productId = 4L, productName = "test1"),
                PopularProductAggregateView(productId = 5L, productName = "test1"),
            )
            given(productStatisticRepository.findTop5BestSellingProductsSince(any())).willReturn(topProducts)
            doNothing().whenever(productStatisticRepository).saveAllPopularProducts(any())

            // when & then: 예외 없으면 성공
            scheduler.generatePopularProducts()
        }

        @Test
        fun `인기 상품 조회 결과가 null이면 예외가 발생한다`() {
            // given
            given(productStatisticRepository.findTop5BestSellingProductsSince(any())).willReturn(null)

            // expect
            assertThatThrownBy {
                scheduler.generatePopularProducts()
            }.isInstanceOf(BusinessException::class.java)
                .hasMessageContaining(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND.message)
        }

        @Test
        fun `인기 상품이 비어있으면 예외가 발생한다`() {
            // given
            given(productStatisticRepository.findTop5BestSellingProductsSince(any())).willReturn(emptyList())

            // expect
            assertThatThrownBy {
                scheduler.generatePopularProducts()
            }.isInstanceOf(BusinessException::class.java)
                .hasMessageContaining(BusinessErrorCode.PRODUCT_OPTIONS_NOT_FOUND.message)
        }
    }
}