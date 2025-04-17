package kr.hhplus.be.server.domain.statistics.product

import kr.hhplus.be.server.domain.statistics.product.model.PopularProduct
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductView
import java.time.LocalDateTime

interface ProductStatisticRepository {

    fun findAllPopularProduct(): List<PopularProductView>?

    fun deleteAllPopularProducts(): Boolean

    fun saveAllPopularProducts(popularProduct: List<PopularProduct>): Boolean

    fun findTop5BestProduct(startDate: LocalDateTime): List<PopularProductView>?
}