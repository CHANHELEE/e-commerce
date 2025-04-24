package kr.hhplus.be.server.support

import kr.hhplus.be.server.infrastructure.persistence.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.coupon.UserCouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.order.OrderJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.order.OrderProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.point.PointJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductOptionJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.product.ProductStockJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.statistics.PopularProductJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.user.UserJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class E2eTestSupport {

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
    lateinit var popularProductJpaRepository: PopularProductJpaRepository

    lateinit var webTestClient: WebTestClient

    @LocalServerPort
    var port: Int = 28080

}