package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Repository

@Repository
interface CouponIssueRequestRepository {

    fun saveIssueRequest(userId: Long, couponId: Long)

    fun findRequestForIssue(couponId: Long): Long?

    fun findCouponAmount(couponId: Long): Long?

    fun saveRequestingUser(userId: Long, couponId: Long): Boolean

    fun decreaseCouponAmount(couponId: Long): Long

    fun deleteCouponAmount(couponId: Long)
}