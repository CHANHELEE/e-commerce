package kr.hhplus.be.server.presentation.order

import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.UserCouponEntity
import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductOptionEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductStockEntity
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.support.E2eTestSupport
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

class OrderControllerE2eTest : E2eTestSupport() {

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `POST - 주문 생성 성공`() {

        // given
        val coupon = couponJpaRepository.save(
            CouponEntity(
                amount = 50L,
                name = "test_coupon_1",
                discountPrice = 1000L,
            )
        )
        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        pointJpaRepository.save(
            PointEntity(
                userId = user.id!!,
                point = 1_000_000,
            )
        )

        val userCoupon = userCouponJpaRepository.save(
            UserCouponEntity(
                userId = user.id!!,
                coupon = coupon!!,
            )
        )

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

        val productStock = productStockJpaRepository.save(
            ProductStockEntity(
                productId = product.id,
                productOptionId = productOption.id,
                stock = 1_000L
            )
        )

        val request = mapOf(
            "userId" to user.id!!,
            "couponId" to coupon.id,
            "orderedProduct" to listOf(
                mapOf(
                    "productId" to product.id,
                    "productOptionId" to productOption.id,
                    "quantity" to 1
                )
            )
        )

        // when && then
        webTestClient.post()
            .uri("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.id").exists()
            .jsonPath("$.data.userId").isEqualTo(user.id!!)
    }
}