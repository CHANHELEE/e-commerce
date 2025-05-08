package kr.hhplus.be.server.infrastructure.persistence.statistics

import kr.hhplus.be.server.domain.statistics.product.ProductStatisticRepository
import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct
import kr.hhplus.be.server.domain.statistics.product.model.PopularProductAggregateView
import kr.hhplus.be.server.infrastructure.persistence.statistics.model.entity.PopularProductEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProductStatisticRepositoryImpl(
    private val popularProductJpaRepository: PopularProductJpaRepository,
    private val popularProductStatisticCacheRepository: ProductStatisticCacheRepository
) : ProductStatisticRepository {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun findAllPopularProducts(): List<PopularProduct>? {

        return try {
            popularProductStatisticCacheRepository.findPopularProducts() ?: run {
                popularProductJpaRepository.findAll().map { it.toDomain() }
            }
        } catch (e: Exception) {
            logger.error("Failed to fetch popular products from cache", e)
            popularProductJpaRepository.findAll().map { it.toDomain() }
        }
    }

    override fun deleteAllPopularProducts() {
        popularProductJpaRepository.deleteAllInBatch()
    }

    override fun saveAllPopularProducts(popularProduct: List<PopularProduct>) {
        val products = popularProductJpaRepository.saveAll(popularProduct.map { PopularProductEntity.from(it) })
        try {
            popularProductStatisticCacheRepository.saveAll(products.map { it.toDomain() })
        } catch (e: Exception) {
            logger.error("Failed to cache popular products. Products count: ${popularProduct.size}", e)
        }
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