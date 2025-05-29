package kr.hhplus.be.server.infrastructure.persistence.coupon

import kr.hhplus.be.server.domain.coupon.CouponIssueRequestRepository
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueRequestRedisRepository
import org.springframework.stereotype.Repository

@Repository
class CouponIssueRequestRepositoryImpl(
    private val couponIssueRequestRedisRepository: CouponIssueRequestRedisRepository
) : CouponIssueRequestRepository {

    override fun isAvailableCoupon(couponId: Long): Boolean {
        return couponIssueRequestRedisRepository.isAvailableCoupon(couponId)
    }

    override fun deleteAvailableCoupon(couponId: Long) {
        return couponIssueRequestRedisRepository.deleteAvailableCoupon(couponId)
    }

    override fun saveAvailableCoupon(couponId: Long) {
        couponIssueRequestRedisRepository.saveAvailableCoupon(couponId)
    }

    override fun saveResult(requestId: String, code: String) {
        return couponIssueRequestRedisRepository.saveResult(requestId, code)
    }
}