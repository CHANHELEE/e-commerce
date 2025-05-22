package kr.hhplus.be.server.support

import kr.hhplus.be.server.application.coupon.CouponScheduler
import kr.hhplus.be.server.config.TestEventPublisherConfig
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.infrastructure.persistence.coupon.jpa.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.coupon.jpa.UserCouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.order.OrderJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.order.OrderProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.point.PointJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductOptionJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductStockJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.statistics.jpa.PopularProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.statistics.redis.ProductStatisticRedisRepository
import kr.hhplus.be.server.infrastructure.persistence.user.UserJpaRepository
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestEventPublisherConfig::class)
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
    lateinit var productStatisticRedisRepository: ProductStatisticRedisRepository

    @Autowired
    lateinit var popularProductJpaRepository: PopularProductJpaRepository

    @Autowired
    lateinit var redissonClient: RedissonClient

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    lateinit var couponService: CouponService

    @Autowired
    lateinit var couponScheduler: CouponScheduler

    @MockitoSpyBean
    lateinit var applicationEventPublisher: ApplicationEventPublisher

}