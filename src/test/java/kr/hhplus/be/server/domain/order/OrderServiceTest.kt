package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
                quantity = 2L
            ),
            OrderCommand.PlaceOrderProduct(
                productOptionId = 20L,
                orderId = 1L,
                productPrice = 2000L,
                quantity = 1L
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
}