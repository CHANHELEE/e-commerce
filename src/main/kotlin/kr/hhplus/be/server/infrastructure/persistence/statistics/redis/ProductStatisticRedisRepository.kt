package kr.hhplus.be.server.infrastructure.persistence.statistics.redis

import kr.hhplus.be.server.domain.statistics.product.model.entity.PopularProduct
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ProductStatisticRedisRepository(
    private val redissonClient: RedissonClient
) : ProductStatisticCacheRepository {

    private val key = "cache:popular-products"

    private val ttl: Duration = Duration.ofHours(25)

    override fun findPopularProducts(): List<PopularProduct>? {
        val bucket = redissonClient.getBucket<List<PopularProduct>>(key)
        return bucket.get()
    }

    override fun saveAll(products: List<PopularProduct>) {
        val bucket = redissonClient.getBucket<List<PopularProduct>>(key)
        bucket.set(products, ttl)
    }
}