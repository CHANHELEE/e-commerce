package kr.hhplus.be.server.infrastructure.persistence.statistics

import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductAggregateView
import kr.hhplus.be.server.infrastructure.persistence.statistics.model.entity.PopularProductEntity
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProductStatisticRepositoryImpl(
    private val popularProductJpaRepository: PopularProductJpaRepository,
) : ProductStatisticRepository {

    override fun findAllPopularProducts(): List<PopularProduct>? {
        val popularProducts = popularProductJpaRepository.findAll()

        return popularProducts.map { it.toDomain() }
    }

    override fun deleteAllPopularProducts() {
        popularProductJpaRepository.deleteAll()
    }

    override fun saveAllPopularProducts(popularProduct: List<PopularProduct>) {
        popularProductJpaRepository.saveAll(popularProduct.map { PopularProductEntity.from(it) })
    }

    override fun findTop5BestSellingProductsSince(startDate: LocalDateTime): List<PopularProductAggregateView>? {
        return popularProductJpaRepository.findTop5BestSellingProductsSince(startDate)
            ?.map {
                PopularProductAggregateView(
                    productId = it.productId,
                    productName = it.name,
                )
            }
    }
}