package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.*
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    lateinit var orderRepository: OrderRepository

    @InjectMocks
    lateinit var orderService: OrderService

    @Nested
    inner class GetWithLockBy {

        @Test
        fun `주문이 존재하면 반환한다`() {
            // given
            val order = Order(id = 1L, userId = 10L, status = OrderStatus.PENDING)
            given(orderRepository.findWithLockBy(1L)).willReturn(order)

            // when
            val result = orderService.getWithLockBy(OrderCommand.Order(orderId = 1L))

            // then
            assertThat(result)
                .extracting("id", "userId")
                .contains(order.id, order.userId)
        }

        @Test
        fun `주문이 존재하지 않으면 예외를 던진다`() {
            // given
            given(orderRepository.findWithLockBy(1L)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                orderService.getWithLockBy(OrderCommand.Order(orderId = 1L))
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.ORDER_NOT_EXIST)
        }
    }

    @Nested
    inner class GetAllActiveOrderProductsBy {

        @Test
        fun `활성 주문 상품이 존재하면 반환한다`() {
            // given
            val products = listOf(
                OrderProduct(
                    id = 1L,
                    orderId = 1L,
                    productOptionId = 100L,
                    productId = 200L,
                    productPrice = 5000L,
                    quantity = 2L
                )
            )
            given(orderRepository.findAllActiveOrderProductsBy(1L)).willReturn(products)

            // when
            val result = orderService.getAllActiveOrderProductsBy(OrderCommand.Order(orderId = 1L))

            // then
            assertThat(result).hasSize(1)
            assertThat(result[0].productId).isEqualTo(200L)
        }

        @Test
        fun `활성 주문 상품이 없으면 예외를 던진다`() {
            // given
            given(orderRepository.findAllActiveOrderProductsBy(1L)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                orderService.getAllActiveOrderProductsBy(OrderCommand.Order(orderId = 1L))
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.ORDER_PRODUCT_NOT_EXIST)
        }
    }

    @Test
    fun `주문을 생성하면 주문, 주문 이력, 주문 상품이 저장된다`() {
        // given
        val placeOrder = OrderCommand.PlaceOrder(
            userId = 1L,
            userCouponId = 2L,
            status = OrderStatus.PENDING
        )

        val placeOrderProducts = listOf(
            OrderCommand.PlaceOrderProduct(
                orderId = 0L, // 실제 저장 전에는 0 또는 null
                productId = 100L,
                productOptionId = 200L,
                productPrice = 3000L,
                quantity = 2L
            )
        )

        val savedOrder = Order(
            id = 10L,
            userId = placeOrder.userId,
            userCouponId = placeOrder.userCouponId,
            status = placeOrder.status
        )

        given(orderRepository.save(any())).willReturn(savedOrder)

        // when
        val result = orderService.order(placeOrder, placeOrderProducts)

        // then
        assertThat(result.id).isEqualTo(savedOrder.id)
        assertThat(result.status).isEqualTo(OrderStatus.PENDING)

        then(orderRepository).should().save(check<Order> {
            assertThat(it.userId).isEqualTo(placeOrder.userId)
            assertThat(it.userCouponId).isEqualTo(placeOrder.userCouponId)
        })

        then(orderRepository).should().saveAllOrderProducts(check {
            assertThat(it).hasSize(1)
            assertThat(it[0].productId).isEqualTo(100L)
            assertThat(it[0].orderId).isEqualTo(savedOrder.id)
        })
    }
}