package kr.hhplus.be.server.infrastructure.persistence.coupon.redis

import kr.hhplus.be.server.domain.coupon.model.CouponsIssueResult
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class CouponIssueRequestRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun isAvailableCoupon(couponId: Long): Boolean {
        return redisTemplate.opsForSet()
            .isMember(CouponIssueKeyPrefix.AVAILABLE.prefix, couponId.toString()) == true
    }

    fun deleteAvailableCoupon(couponId: Long) {
        redisTemplate.opsForSet()
            .remove(CouponIssueKeyPrefix.AVAILABLE.prefix, couponId.toString())
    }

    fun saveAvailableCoupon(couponId: Long) {
        redisTemplate.opsForSet()
            .add(CouponIssueKeyPrefix.AVAILABLE.prefix, couponId.toString())
    }

    fun saveResult(requestId: String, code: String) {
        return redisTemplate.opsForHash<String, String>()
            .put(CouponIssueKeyPrefix.RESULT.prefix, requestId, code)
    }

    fun findResult(requestId: String): CouponsIssueResult? {
        val code = redisTemplate.opsForHash<String, String>()
            .get(CouponIssueKeyPrefix.RESULT.prefix, requestId)
            ?: return null

        return CouponsIssueResult(requestId, code)
    }
}