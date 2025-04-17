package kr.hhplus.be.server.support

import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.infrastructure.persistence.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.persistence.point.PointJpaRepository
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
}