package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponsIssueResult
import org.springframework.stereotype.Repository

@Repository
interface CouponIssueRequestRepository {

    fun isAvailableCoupon(couponId: Long): Boolean

    fun deleteAvailableCoupon(couponId: Long)

    fun saveAvailableCoupon(couponId: Long)

    fun saveResult(requestId: String, code: String)

    fun getResult(requestId: String): CouponsIssueResult?
}