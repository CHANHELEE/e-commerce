package kr.hhplus.be.server.presentation.product

import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductOptionEntity
import kr.hhplus.be.server.support.E2eTestSupport
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient

class ProductControllerE2eTest : E2eTestSupport() {

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `GET - 상품 단건 조회`() {

        //given
        val product = productJpaRepository.save(
            ProductEntity(
                name = "test_product",
                price = 10_000L,
            )
        )

        //when & then
        webTestClient.get()
            .uri("/products/${product.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.id").isEqualTo(product.id)
            .jsonPath("$.data.name").isEqualTo(product.name)
            .jsonPath("$.data.price").isEqualTo(product.price)
    }

    @Test
    fun `GET - 상품 옵션 목록 조회`() {

        //given
        val product = productJpaRepository.save(
            ProductEntity(
                name = "test_product_1",
                price = 10_000L,
            )
        )

        val productOption = productOptionJpaRepository.save(
            ProductOptionEntity(
                productId = product.id!!,
                size = "대",
                stock = 1_000L,
            )
        )

        // when & then
        webTestClient.get()
            .uri("/products/${product.id}/options")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data").isArray
            .jsonPath("$.data[0].id").isEqualTo(product.id)
            .jsonPath("$.data[0].name").isEqualTo(product.name)
            .jsonPath("$.data[0].price").isEqualTo(product.price)
            .jsonPath("$.data[0].size").isEqualTo(productOption.size)
            .jsonPath("$.data[0].stock").isEqualTo(productOption.stock)
    }
}