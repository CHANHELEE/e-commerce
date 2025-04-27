package kr.hhplus.be.server.support

import kr.hhplus.be.server.infrastructure.persistence.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.coupon.UserCouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.order.OrderJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.order.OrderProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.point.PointJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductOptionJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductStockJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.user.UserJpaRepository
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTestSupport {

    @Autowired
    lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    lateinit var pointJpaRepository: PointJpaRepository

    @Autowired
    lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    lateinit var productOptionJpaRepository: ProductOptionJpaRepository

    @Autowired
    lateinit var productStockJpaRepository: ProductStockJpaRepository

    @Autowired
    lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    lateinit var userCouponJpaRepository: UserCouponJpaRepository

    @Autowired
    lateinit var orderProductJpaRepository: OrderProductJpaRepository

    @Autowired
    lateinit var redissonClient: RedissonClient
}