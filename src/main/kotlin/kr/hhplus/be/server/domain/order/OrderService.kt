package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.order.model.OrderHistory
import kr.hhplus.be.server.domain.order.model.OrderProduct
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    fun save(orderCommand: OrderCommand.PlaceOrder): Order {
        return orderRepository.save(
            Order(
                userId = orderCommand.userId,
                userCouponId = orderCommand.userCouponId,
                status = orderCommand.status
            )
        )
    }

    fun saveHistory(orderCommand: OrderCommand.PlaceOrderHistory): OrderHistory {
        return orderRepository.saveHistory(
            OrderHistory(
                orderId = orderCommand.orderId,
                orderStatus = orderCommand.status,
            )
        )
    }

    fun saveOrderProducts(orderCommand: List<OrderCommand.PlaceOrderProduct>) {
        orderRepository.saveAllOrderProducts(orderCommand.map {
            OrderProduct(
                productOptionId = it.productOptionId,
                orderId = it.orderId,
                productPrice = it.productPrice,
                quantity = it.quantity,
                productId = it.productId,
            )
        })
    }

    fun getWithLockBy(orderCommand: OrderCommand.Order): Order =
        orderRepository.findWithLockBy(orderCommand.orderId)
            ?: throw BusinessException(BusinessErrorCode.ORDER_NOT_EXIST)

    fun getAllActiveOrderProductsBy(orderCommand: OrderCommand.Order): List<OrderProduct> =
        orderRepository.findAllActiveOrderProductsBy(orderCommand.orderId)
            ?: throw BusinessException(BusinessErrorCode.ORDER_PRODUCT_NOT_EXIST)
}