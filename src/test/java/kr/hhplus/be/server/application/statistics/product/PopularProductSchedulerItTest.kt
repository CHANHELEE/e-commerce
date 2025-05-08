package kr.hhplus.be.server.application.statistics.product


import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderProductEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductOptionEntity
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PopularProductSchedulerItTest @Autowired constructor(private val scheduler: PopularProductScheduler) :
    IntegrationTestSupport() {

    @Test
    fun `정상적으로 인기 상품이 저장 된다`() {

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

        val productOptions = products.map { product ->
            ProductOptionEntity(
                productId = product.id!!, size = "대", stock = 1_000L
            )
        }.let { productOptionJpaRepository.saveAll(it) }

        val user = userJpaRepository.save(
            UserEntity(name = "user")
        )

        val order = orderJpaRepository.save(
            OrderEntity(
                userId = user.id!!, status = OrderStatus.SUCCESS
            )
        )

        val orderProducts = productOptions.mapIndexed { index, option ->
            OrderProductEntity(
                orderId = order.id!!,
                productId = option.productId,
                productOptionId = option.id!!,
                productPrice = 10_000L,
                amount = index + 2L
            )
        }
        orderProductJpaRepository.saveAll(orderProducts)

        //when
        scheduler.generatePopularProducts()

        //then
        val cachedData = productStatisticRedisRepository.findPopularProducts()
        val rdbData = popularProductJpaRepository.findAll()

        assertThat(cachedData).hasSizeGreaterThan(0)
        assertThat(rdbData).hasSizeGreaterThan(0)
        assertThat(cachedData).hasSameSizeAs(rdbData)

        // 조회 시 Ranking 과 RDB 저장 된 Ranking 값 검증
        // (1) productId -> amount 매핑 (ex. 1001L -> 3L)
        val amountMap = orderProducts.associateBy({ it.productId }, { it.amount })

        // (2) amount 기준으로 내림차순 정렬한 productId 리스트
        val expectedRanking = amountMap.entries.sortedByDescending { it.value }.mapIndexed { index, entry ->
            entry.key to (index + 1) // productId to ranking
        }.toMap()

        // (3) rdbData의 productId, ranking 이 예상과 일치하는지 검증
        rdbData.forEach { product ->
            val expectedRank = expectedRanking[product.productId]
            assertThat(product.ranking).withFailMessage("productId=${product.productId} expected ranking=$expectedRank, actual=${product.ranking}")
                .isEqualTo(expectedRank)
        }

        // RDB , Cache 간 값 검증
        rdbData!!.forEach { product ->
            val matched = cachedData!!.find { it.id == product.id }
            assertThat(matched).withFailMessage("No matching product found where productId == ${product.id}")
                .isNotNull()

            assertThat(matched!!.ranking).withFailMessage("Matching product found, but ranking not match (${matched.ranking} != ${product.ranking})")
                .isEqualTo(product.ranking)
        }
    }
}