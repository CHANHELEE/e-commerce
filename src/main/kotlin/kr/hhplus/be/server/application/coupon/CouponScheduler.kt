package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueKeyPrefix
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CouponScheduler(
    private val redisTemplate: RedisTemplate<String, String>,
    private val couponService: CouponService,
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 1000)
    fun issueCoupons() {
        val scanOptions = ScanOptions.scanOptions()
            .match(CouponIssueKeyPrefix.ISSUE_TARGET.prefix + "*")
            .build()

        val keyCommands = redisTemplate.execute { it.keyCommands() } ?: return
        val cursor = keyCommands.scan(scanOptions)
        cursor.use {
            for (rawKey in cursor) {
                val key = String(rawKey)
                val couponId = key.removePrefix(CouponIssueKeyPrefix.ISSUE_TARGET.prefix)
                repeat(20) {
                    val userId = couponService.findRequestForIssue(couponId.toLong()) ?: run {
                        redisTemplate.delete(key)
                        return@repeat
                    }
                    try {
                        couponService.issue(
                            CouponCommand.Issue(
                                userId = userId.toLong(),
                                couponId = couponId.toLong()
                            )
                        )
                    } catch (e: Exception) {
                        logger.error("userId = $userId, failed to issue couponId = $couponId")
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}