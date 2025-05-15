package kr.hhplus.be.server.infrastructure.persistence.coupon.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class CouponIssueRequestRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun findCouponAmount(couponId: Long): Long? {

        return redisTemplate.opsForValue().get("${CouponIssueKeyPrefix.COUPON_AMOUNT.prefix}$couponId")?.toLongOrNull()
    }


    fun decreaseCouponAmount(couponId: Long): Long {
        return redisTemplate.opsForValue()
            .decrement("${CouponIssueKeyPrefix.COUPON_AMOUNT.prefix}$couponId")!!
    }

    fun saveRequestingUser(userId: Long, couponId: Long): Boolean {
        return redisTemplate.opsForSet()
            .add("${CouponIssueKeyPrefix.REQUESTING_USER.prefix}$couponId", userId.toString()) == 1L
    }

    fun deleteCouponAmount(couponId: Long): Boolean {
        return redisTemplate.delete("${CouponIssueKeyPrefix.COUPON_AMOUNT.prefix}$couponId")
    }

    fun saveIssueRequest(userId: Long, couponId: Long) {
        redisTemplate.opsForList()
            .rightPush("${CouponIssueKeyPrefix.ISSUE_TARGET.prefix}$couponId", userId.toString())
    }
}