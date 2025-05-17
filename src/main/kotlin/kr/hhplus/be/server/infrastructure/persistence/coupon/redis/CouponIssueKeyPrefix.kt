package kr.hhplus.be.server.infrastructure.persistence.coupon.redis

enum class CouponIssueKeyPrefix(val prefix: String) {
    COUPON_AMOUNT("cache:coupon-amount:"),
    REQUESTING_USER("set:coupon-requesting-users:"),
    ISSUE_TARGET("queue:coupon-issue:");
}