package kr.hhplus.be.server.domain.statistics.product


import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given

@ExtendWith(MockitoExtension::class)
class ProductStatisticServiceTest {

    @Mock
    lateinit var productStatisticRepository: ProductStatisticRepository

    @InjectMocks
    lateinit var productStatisticService: ProductStatisticService

    @Nested
    inner class GetAllPopularProducts {

        @Test
        fun `인기 상품이 존재하면 리스트를 반환한다`() {
            // given
            val popularList = listOf(
                PopularProductView(id = 1L, productName = "test", rank = 1),
                PopularProductView(id = 2L, productName = "test", rank = 2),
                PopularProductView(id = 3L, productName = "test", rank = 3),
                PopularProductView(id = 4L, productName = "test", rank = 4),
                PopularProductView(id = 5L, productName = "test", rank = 5),
            )
            given(productStatisticRepository.findAllPopularProduct()).willReturn(popularList)

            // when
            val result = productStatisticService.getAllPopularProducts()

            // then
            assertThat(result).hasSize(5)
        }

        @Test
        fun `인기 상품이 존재하지 않으면 예외가 발생한다`() {
            // given
            given(productStatisticRepository.findAllPopularProduct()).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                productStatisticService.getAllPopularProducts()
            }

            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.POPULAR_PRODUCTS_NOT_EXIST)
        }
    }
}