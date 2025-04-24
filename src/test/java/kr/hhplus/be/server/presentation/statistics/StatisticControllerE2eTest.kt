package kr.hhplus.be.server.presentation.statistics

import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.infrastructure.persistence.statistics.model.entity.PopularProductEntity
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

        val product = productJpaRepository.save(
            ProductEntity(
                name = "test_product",
                price = 10_000L,
            )
        )

        val popularProducts = popularProductJpaRepository.save(
            PopularProductEntity(
                productId = product.id,
                name = product.name,
                ranking = 1,
            )
        )

        webTestClient.get()
            .uri("/statistic/ranks/top-five")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data").isArray
            .jsonPath("$.data[0].productId").isEqualTo(product.id)
            .jsonPath("$.data[0].productName").isEqualTo(product.name)
            .jsonPath("$.data[0].rank").isEqualTo(popularProducts.ranking)
    }
}