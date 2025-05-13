package kr.hhplus.be.server.presentation.statistics

import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.support.E2eTestSupport
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient

class StatisticControllerE2eTest : E2eTestSupport() {

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `GET - 가장 많이 팔린 상위 5개 상품 조회`() {

        //given
        val products = productJpaRepository.saveAll(
            listOf(
                ProductEntity(name = "test1", price = 10_000L),
                ProductEntity(name = "test2", price = 10_000L),
                ProductEntity(name = "test3", price = 10_000L),
                ProductEntity(name = "test4", price = 10_000L),
                ProductEntity(name = "test5", price = 10_000L)
            )
        )

        popularProductJpaRepository.deleteAllInBatch()

        val popularProducts = products.mapIndexed { index, product ->
            PopularProduct(
                productId = product.id!!,
                name = product.name,
                ranking = index + 1
            )
        }
        productStatisticRepository.saveAllPopularProducts(popularProducts)

        // when && then
        webTestClient.get()
            .uri("/statistic/ranks/top-five")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data").isArray
            .jsonPath("$.data.length()").isEqualTo(5)
    }
}