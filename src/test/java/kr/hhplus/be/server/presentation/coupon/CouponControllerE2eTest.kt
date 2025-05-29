package kr.hhplus.be.server.presentation.coupon

import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueKeyPrefix
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.presentation.coupon.model.CouponRequest
import kr.hhplus.be.server.support.E2eTestSupport
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

class CouponControllerE2eTest : E2eTestSupport() {

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `GET - 특정 쿠폰을 조회한다`() {

        //given
        val coupon = couponJpaRepository.save(
            CouponEntity(
                amount = 50L,
                name = "test_coupon",
                discountPrice = 1000L,
            )
        )

        // when & then
        webTestClient.get()
            .uri("/coupons/${coupon.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.id").isEqualTo(coupon.id)
            .jsonPath("$.data.amount").isEqualTo(coupon.amount)
            .jsonPath("$.data.name").isEqualTo(coupon.name)
            .jsonPath("$.data.discountPrice").isEqualTo(coupon.discountPrice)
    }

    @Test
    fun `POST - 쿠폰을 발급한다`() {
        // given
        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        val couponAmount = 50L
        val coupon = couponJpaRepository.save(
            CouponEntity(
                amount = couponAmount,
                name = "test_coupon",
                discountPrice = 1000L,
            )
        )
        val requestId = coupon.id.toString() + "-" + user.id.toString()
        couponIssueRequestRepository.saveAvailableCoupon(couponId = coupon.id)

        val request = CouponRequest.Issue(
            couponId = coupon.id,
            userId = user.id!!,
        )

        // when & then
        webTestClient.post()
            .uri("/coupons")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.requestId").isEqualTo(requestId)
    }
}