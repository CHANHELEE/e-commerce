package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.order.model.OrderCriteria
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.UserCoupon
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.model.Point
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductCommand
import kr.hhplus.be.server.domain.product.model.ProductStock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.check

@ExtendWith(MockitoExtension::class)
class OrderFacadeTest {

    @Mock
    lateinit var productService: ProductService

    @Mock
    lateinit var couponService: CouponService

    @Mock
    lateinit var pointService: PointService

    @Mock
    lateinit var orderService: OrderService

    @InjectMocks
    lateinit var orderFacade: OrderFacade

    @Test
    fun `주문이 정상적으로 처리된다`() {

        // given
        val userId = 1L
        val couponId = 10L
        val productId = 100L
        val productOptionId = 200L
        val quantity = 2L
        val price = 5000L

        val criteria = OrderCriteria.PlaceOrder(
            userId = userId,
            couponId = couponId,
            orderedProduct = listOf(
                OrderCriteria.OrderedProduct(
                    productId = productId,
                    productOptionId = productOptionId,
                    quantity = quantity
                )
            )
        )

        val userCoupon = UserCoupon(
            id = 1L,
            userId = userId,
            couponId = couponId,
            usedAt = null
        )
        given(couponService.getUserCouponBy(CouponCommand.UserCoupon(userId, couponId))).willReturn(userCoupon)

        val point = Point(id = 1L, userId = userId, point = 100000L)
        given(pointService.getPoint(PointCommand.Point(userId))).willReturn(point)

        given(productService.getProductStockBy(ProductCommand.ProductStock(productId, productOptionId)))
            .willReturn(ProductStock(productId = productId, productOptionId = productOptionId, stock = 10))

        given(productService.getProductBy(ProductCommand.Product(productId)))
            .willReturn(Product(id = productId, name = "상품", price = price))

        val savedOrder = Order(id = 123L, userId = userId, userCouponId = couponId, status = OrderStatus.PENDING)
        given(orderService.save(any())).willReturn(savedOrder)

        // when
        val result = orderFacade.placeOrder(criteria)

        // then
        assertThat(result.id).isEqualTo(savedOrder.id)
        assertThat(result.status).isEqualTo(OrderStatus.PENDING)

        then(orderService).should().saveHistory(
            OrderCommand.PlaceOrderHistory(orderId = savedOrder.id, status = savedOrder.status)
        )

        then(orderService).should().saveOrderProducts(check {
            assertThat(it).hasSize(1)
            val product = it[0]
            assertThat(product.productOptionId).isEqualTo(productOptionId)
            assertThat(product.productPrice).isEqualTo(price)
            assertThat(product.orderId).isEqualTo(savedOrder.id)
            assertThat(product.quantity).isEqualTo(quantity)
        })
    }
}
