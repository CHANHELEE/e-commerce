package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.order.model.OrderCriteria
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.UserCouponView
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.order.model.OrderView
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.point.model.PointView
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.ProductCommand
import kr.hhplus.be.server.domain.product.model.ProductStockView
import kr.hhplus.be.server.domain.product.model.ProductView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.check
import java.time.LocalDateTime

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

        val userCoupon = UserCouponView(
            id = 1L,
            userId = userId,
            couponId = couponId,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            usedAt = null,
        )

        val point = mock(PointView::class.java)
        given(couponService.validateUse(CouponCommand.UserCoupon(userId, couponId))).willReturn(userCoupon)

        given(pointService.validateUsable(PointCommand.Point(userId))).willReturn(point)

        given(productService.validateStock(ProductCommand.ProductStock(productId, productOptionId)))
            .willReturn(
                ProductStockView(
                    id = 1L,
                    productId = productId,
                    productOptionId = productOptionId,
                    stock = 10L,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )
            )

        given(productService.getProductBy(ProductCommand.Product(productId)))
            .willReturn(
                ProductView(
                    id = productId,
                    name = "상품",
                    price = price,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )

        val savedOrder = OrderView(
            id = 123L,
            userId = userId,
            userCouponId = couponId,
            status = OrderStatus.PENDING,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = LocalDateTime.now()
        )
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
