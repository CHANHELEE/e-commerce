package kr.hhplus.be.server.application.payment

import kr.hhplus.be.server.application.payment.model.PaymentCriteria
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.Coupon
import kr.hhplus.be.server.domain.coupon.model.UserCoupon
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.order.model.OrderProduct
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.payment.model.Payment
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PaymentFacadeTest {

    @Mock
    lateinit var productService: ProductService

    @Mock
    lateinit var couponService: CouponService

    @Mock
    lateinit var pointService: PointService

    @Mock
    lateinit var orderService: OrderService

    @Mock
    lateinit var paymentService: PaymentService

    @InjectMocks
    lateinit var paymentFacade: PaymentFacade

    @Test
    fun `결제가 정상적으로 수행된다`() {

        // given
        val userId = 1L
        val orderId = 100L
        val couponId = 999L

        val paymentCriteria = PaymentCriteria.PlacePayment(
            orderId = orderId
        )

        val order = Order(id = orderId, userId = userId, userCouponId = couponId, status = OrderStatus.SUCCESS)
        val userCoupon =
            UserCoupon(id = 1L, userId = userId, couponId = couponId, usedAt = null, createdAt = LocalDateTime.now())
        val coupon = Coupon(id = couponId, amount = 10000L, discountPrice = 1000L, name = "할인쿠폰")

        val orderProduct = OrderProduct(
            id = 1L,
            orderId = orderId,
            productOptionId = 10L,
            productId = 1000L,
            productPrice = 5000L,
            quantity = 2L
        )

        val stock = ProductStock(
            id = 1L,
            productId = orderProduct.productId,
            productOptionId = orderProduct.productOptionId,
            stock = 10
        )

        val product = Product(
            id = orderProduct.productId,
            name = "테스트상품",
            price = 5000L
        )

        val savedPayment = Payment(
            id = 1L,
            orderId = orderId,
            originTotalPrice = 10000L,
            payTotalPrice = 9000L,
            discountPrice = 1000L,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(orderService.getWithLockBy(OrderCommand.Order(orderId))).willReturn(order)
        given(couponService.getUserCouponWithLockBy(CouponCommand.UserCoupon(userId, couponId))).willReturn(userCoupon)
        given(couponService.updateUserCoupon(any())).willReturn(userCoupon)
        given(couponService.getCouponBy(CouponCommand.Coupon(couponId))).willReturn(coupon)
        given(orderService.getAllActiveOrderProductsBy(OrderCommand.Order(orderId))).willReturn(listOf(orderProduct))
        given(productService.getProductStockWithLockBy(any())).willReturn(stock)
        given(productService.getProductBy(any())).willReturn(product)
        given(paymentService.save(any())).willReturn(savedPayment)

        // when
        val result = paymentFacade.pay(paymentCriteria)

        // then
        then(pointService).should().usePoint(check {
            assertThat(it.userId).isEqualTo(userId)
            assertThat(it.amount).isEqualTo(9000L)
        })

        then(paymentService).should().save(check {
            assertThat(it.orderId).isEqualTo(orderId)
            assertThat(it.originTotalPrice).isEqualTo(10000L)
            assertThat(it.payTotalPrice).isEqualTo(9000L)
            assertThat(it.discountPrice).isEqualTo(1000L)
        })

        assertThat(result.orderId).isEqualTo(orderId)
        assertThat(result.payTotalPrice).isEqualTo(9000L)

        verify(orderService, times(1)).getWithLockBy(OrderCommand.Order(orderId))
        verify(couponService, times(1)).getUserCouponWithLockBy(CouponCommand.UserCoupon(userId, couponId))
        verify(couponService, times(1)).updateUserCoupon(any())
        verify(couponService, times(1)).getCouponBy(CouponCommand.Coupon(couponId))
        verify(orderService, times(1)).getAllActiveOrderProductsBy(OrderCommand.Order(orderId))
        verify(productService, times(1)).getProductStockWithLockBy(any())
        verify(productService, times(1)).getProductBy(any())
        verify(pointService, times(1)).usePoint(any())
        verify(paymentService, times(1)).save(any())
    }
}