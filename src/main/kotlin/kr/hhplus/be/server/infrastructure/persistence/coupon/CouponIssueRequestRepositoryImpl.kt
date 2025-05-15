package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.domain.coupon.CouponIssueRequestRepository
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueRequestRedisRepository
import org.springframework.stereotype.Repository

@Repository
class CouponIssueRequestRepositoryImpl(
    private val couponIssueRequestRedisRepository: CouponIssueRequestRedisRepository
) : CouponIssueRequestRepository {

    override fun saveIssueRequest(userId: Long, couponId: Long) {
        couponIssueRequestRedisRepository.saveIssueRequest(userId = userId, couponId = couponId)
    }

    override fun findRequestToIssue(couponId: Long): Long? {
        TODO("Not yet implemented")
    }

    override fun getCouponAmount(couponId: Long): Long? {
        return couponIssueRequestRedisRepository.findCouponAmount(couponId)
    }

    override fun saveRequestingUser(userId: Long, couponId: Long): Boolean {
        return couponIssueRequestRedisRepository.saveRequestingUser(userId, couponId)
    }

    override fun decreaseCouponAmount(couponId: Long): Long {
        return couponIssueRequestRedisRepository.decreaseCouponAmount(couponId)
    }

    override fun deleteCouponAmount(couponId: Long) {
        couponIssueRequestRedisRepository.deleteCouponAmount(couponId)
    }
}