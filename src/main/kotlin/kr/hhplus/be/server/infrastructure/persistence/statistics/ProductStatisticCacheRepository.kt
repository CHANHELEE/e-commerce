package kr.hhplus.be.server.infrastructure.persistence.statistics

import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct

interface ProductStatisticCacheRepository {

    fun findPopularProducts(): List<PopularProduct>?

    fun saveAll(products: List<PopularProduct>)
}