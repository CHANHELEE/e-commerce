package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Repository

@Repository
interface CouponIssueRequestRepository {

    fun saveIssueRequest(userId: Long, couponId: Long)

    fun findRequestToIssue(couponId: Long): Long?

    fun getCouponAmount(couponId: Long): Long?

    fun saveRequestingUser(userId: Long, couponId: Long): Boolean

    fun decreaseCouponAmount(couponId: Long): Long

    fun deleteCouponAmount(couponId: Long)
}