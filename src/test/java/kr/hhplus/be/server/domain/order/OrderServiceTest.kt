package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.*
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

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    lateinit var orderRepository: OrderRepository

    @InjectMocks
    lateinit var orderService: OrderService

    @Test
    fun `save - 주문이 정상적으로 저장된다`() {
        // given
        val command = OrderCommand.PlaceOrder(
            userId = 1L,
            userCouponId = 100L,
            status = OrderStatus.PENDING
        )

        val saved = Order(
            id = 1L,
            userId = command.userId,
            userCouponId = command.userCouponId,
            status = command.status
        )

        given(orderRepository.save(any())).willReturn(saved)

        // when
        val result = orderService.save(command)

        // then
        then(orderRepository).should().save(check {
            assertThat(it.userId).isEqualTo(command.userId)
            assertThat(it.userCouponId).isEqualTo(command.userCouponId)
            assertThat(it.status).isEqualTo(command.status)
        })

        assertThat(result.id).isEqualTo(1L)
    }

    @Test
    fun `saveHistory - 주문 이력이 정상적으로 저장된다`() {
        // given
        val command = OrderCommand.PlaceOrderHistory(
            orderId = 1L,
            status = OrderStatus.SUCCESS
        )

        val history = OrderHistory(
            id = 99L,
            orderId = command.orderId,
            orderStatus = command.status
        )

        given(orderRepository.saveHistory(any())).willReturn(history)

        // when
        val result = orderService.saveHistory(command)

        // then
        then(orderRepository).should().saveHistory(check {
            assertThat(it.orderId).isEqualTo(command.orderId)
            assertThat(it.orderStatus).isEqualTo(command.status)
        })

        assertThat(result.id).isEqualTo(99L)
    }

    @Test
    fun `saveOrderProducts - 주문 상품들이 정상적으로 저장된다`() {
        // given
        val commands = listOf(
            OrderCommand.PlaceOrderProduct(
                productOptionId = 10L,
                orderId = 1L,
                productPrice = 1000L,
                quantity = 2L,
                productId = 2
            ),
            OrderCommand.PlaceOrderProduct(
                productOptionId = 20L,
                orderId = 1L,
                productPrice = 2000L,
                quantity = 1L,
                productId = 3
            )
        )

        // when
        orderService.saveOrderProducts(commands)

        // then
        then(orderRepository).should().saveAllOrderProducts(check {
            assertThat(it).hasSize(2)

            val first = it[0]
            val expectedFirst = commands[0]
            assertThat(first.productOptionId).isEqualTo(expectedFirst.productOptionId)
            assertThat(first.productPrice).isEqualTo(expectedFirst.productPrice)
            assertThat(first.quantity).isEqualTo(expectedFirst.quantity)
            assertThat(first.orderId).isEqualTo(expectedFirst.orderId)
        })
    }

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
            assertThat(result).isEqualTo(order)
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
}