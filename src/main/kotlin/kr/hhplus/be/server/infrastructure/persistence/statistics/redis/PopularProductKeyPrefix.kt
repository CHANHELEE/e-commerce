package kr.hhplus.be.server.infrastructure.persistence.statistics.redis

enum class PopularProductKeyPrefix(val prefix: String) {
    Daily("sorted-set:daily-popular-products:"),
}