package kr.hhplus.be.server.application.payment

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kr.hhplus.be.server.application.payment.model.PaymentCriteria
import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.CouponEntity
import kr.hhplus.be.server.infrastructure.persistence.coupon.entity.UserCouponEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderProductEntity
import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductOptionEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductStockEntity
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class PaymentFacadeItTest : IntegrationTestSupport() {

    @Autowired
    lateinit var paymentFacade: PaymentFacade

    @Test
    fun `정상 결제시 PaymentView 가 반환된다`() {
        // given
        val orderId = createTestOrder()
        val paymentCriteria = PaymentCriteria.PlacePayment(orderId = orderId)

        // when
        val paymentView = paymentFacade.pay(paymentCriteria)

        // then
        assertNotNull(paymentView)
        assertEquals(orderId, paymentView.orderId)
        assertEquals(PaymentStatus.SUCCESS, paymentView.status)
    }

    @Test
    fun `결제 중 예외가 발생하면 주문 상태가 FAIL 로 바뀐다`() {
        // given
        val orderId = createTestOrderThatFails()
        val paymentCriteria = PaymentCriteria.PlacePayment(orderId = orderId)

        // when
        val ex = assertThrows<BusinessException> {
            paymentFacade.pay(paymentCriteria)
        }

        // then
        assertEquals(ex.errorCode, BusinessErrorCode.USER_COUPON_ALREADY_USED)
        val failedOrder = orderJpaRepository.findByIdOrNull(orderId)
        assertEquals(OrderStatus.FAIL, failedOrder!!.status)
    }

    private fun createTestOrder(): Long {

        val coupon = couponJpaRepository.save(
            CouponEntity(
                amount = 50L,
                name = "test_coupon_1",
                discountPrice = 1000L,
            )
        )
        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        val userCoupon = userCouponJpaRepository.save(
            UserCouponEntity(
                userId = user.id!!,
                coupon = coupon!!,
            )
        )

        val point = pointJpaRepository.save(
            PointEntity(
                userId = user.id!!,
                point = 1_000_000,
            )
        )

        val product = productJpaRepository.save(
            ProductEntity(
                name = "test_product_1",
                price = 10_000L,
            )
        )

        val productOption = productOptionJpaRepository.save(
            ProductOptionEntity(
                productId = product.id!!,
                size = "대",
                stock = 1_000L,
            )
        )

        val productStock = productStockJpaRepository.save(
            ProductStockEntity(
                productId = product.id,
                productOptionId = productOption.id,
                stock = 1_000L
            )
        )

        val order = orderJpaRepository.save(
            OrderEntity(
                userId = user.id!!,
                userCouponId = userCoupon.id,
                status = OrderStatus.PENDING,
            )
        )

        val orderProduct = orderProductJpaRepository.save(
            OrderProductEntity(
                orderId = order.id!!,
                productId = product.id,
                productOptionId = productOption.id,
                productPrice = product.price,
                amount = 1L,
            )
        )
        return order.id
    }

    private fun createTestOrderThatFails(): Long {


        val coupon = couponJpaRepository.save(
            CouponEntity(
                amount = 50L,
                name = "test_coupon_1",
                discountPrice = 1000L,
            )
        )
        val user = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        )

        val userCoupon = userCouponJpaRepository.save(
            UserCouponEntity(
                userId = user.id!!,
                coupon = coupon!!,
                usedAt = LocalDateTime.now(), // BusinessErrorCode.USER_COUPON_ALREADY_USED 발생
            )
        )

        val point = pointJpaRepository.save(
            PointEntity(
                userId = user.id!!,
                point = 1_000_000,
            )
        )

        val product = productJpaRepository.save(
            ProductEntity(
                name = "test_product_1",
                price = 10_000L,
            )
        )

        val productOption = productOptionJpaRepository.save(
            ProductOptionEntity(
                productId = product.id!!,
                size = "대",
                stock = 1_000L,
            )
        )

        val productStock = productStockJpaRepository.save(
            ProductStockEntity(
                productId = product.id,
                productOptionId = productOption.id,
                stock = 1_000L
            )
        )

        val order = orderJpaRepository.save(
            OrderEntity(
                userId = user.id!!,
                userCouponId = userCoupon.id,
                status = OrderStatus.PENDING,
            )
        )

        val orderProduct = orderProductJpaRepository.save(
            OrderProductEntity(
                orderId = order.id!!,
                productId = product.id,
                productOptionId = productOption.id,
                productPrice = product.price,
                amount = 1L,
            )
        )
        return order.id
    }
}