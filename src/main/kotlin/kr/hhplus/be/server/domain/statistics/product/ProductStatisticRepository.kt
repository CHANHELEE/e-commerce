package kr.hhplus.be.server.domain.statistics.product

import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductAggregateView
import java.time.LocalDateTime

interface ProductStatisticRepository {

    fun findAllPopularProducts(): List<PopularProduct>?

    fun deleteAllPopularProducts()

    fun saveAllPopularProducts(popularProduct: List<PopularProduct>)

    fun findTop5BestSellingProductsSince(startDate: LocalDateTime): List<PopularProductAggregateView>?

    fun increaseDailyPopularProduct(productId: Long, quantity: Double)
}