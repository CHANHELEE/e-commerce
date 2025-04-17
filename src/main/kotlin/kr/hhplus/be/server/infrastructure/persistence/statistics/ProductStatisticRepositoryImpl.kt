package kr.hhplus.be.server.infrastructure.persistence.statistics

import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.statistics.product.model.PopularProduct
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductView
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProductStatisticRepositoryImpl : ProductStatisticRepository {

    override fun findAllPopularProduct(): List<PopularProductView>? {
        TODO("Not yet implemented")
    }

    override fun deleteAllPopularProducts(): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveAllPopularProducts(popularProduct: List<PopularProduct>): Boolean {
        TODO("Not yet implemented")
    }

    override fun findTop5BestProduct(startDate: LocalDateTime): List<PopularProductView>? {
        TODO("Not yet implemented")
    }
}