package kr.hhplus.be.server.infrastructure.persistence.statistics.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class PopularProductRedisRepository(
    private val redisTemplate: StringRedisTemplate,
) {


    fun increaseDaily(productId: Long, quantity: Double) {

        redisTemplate.opsForZSet()
            .incrementScore(PopularProductKeyPrefix.Daily.prefix, productId.toString(), quantity)
    }


}