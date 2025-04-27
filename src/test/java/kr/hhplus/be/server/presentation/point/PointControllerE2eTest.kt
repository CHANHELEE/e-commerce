package kr.hhplus.be.server.presentation.point

import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.support.E2eTestSupport
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

class PointControllerE2eTest : E2eTestSupport() {

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `GET - 사용자 포인트 조회`() {

        // given
        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        val point = pointJpaRepository.save(
            PointEntity(
                userId = user.id!!,
                point = 1_000_000,
            )
        )

        // when & then
        webTestClient.get()
            .uri("/point?userId=${user.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.userId").isEqualTo(user.id!!)
            .jsonPath("$.data.point").isEqualTo(point.point)
    }

    @Test
    fun `PATCH - 사용자 포인트 충전`() {

        // given
        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        val point = pointJpaRepository.save(
            PointEntity(
                userId = user.id!!,
                point = 1_000_000,
            )
        )

        val chargePoint = 5_000L
        val request = mapOf(
            "userId" to user.id,
            "point" to chargePoint
        )

        // when & then
        webTestClient.patch()
            .uri("/point")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.userId").isEqualTo(point.userId)
            .jsonPath("$.data.point").isEqualTo(point.point + chargePoint) // 충전 후 총 보유 포인트가 5000이 된다고 가정
    }
}