package kr.hhplus.be.server.presentation.payment

import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.UserCouponEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderProductEntity
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

class PaymentControllerE2eTest : E2eTestSupport() {

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `POST - 결제 성공`() {


        val orderId = createTestOrder()
        val request = mapOf(
            "orderId" to orderId
        )

        webTestClient.post()
            .uri("/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.id").exists()
    }

    private fun createTestOrder(): Long {

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

        val order = orderJpaRepository.save(
            OrderEntity(
                userId = user.id!!,
                userCouponId = userCoupon.id,
                status = OrderStatus.PENDING,
            )
        )

        orderProductJpaRepository.save(
            OrderProductEntity(
                orderId = order.id!!,
                productId = product.id,
                productOptionId = productOption.id,
                productPrice = product.price,
                amount = 1L,
            )
        )
        return order.id
    }
}