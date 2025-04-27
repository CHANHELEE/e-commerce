package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.order.model.OrderProductView
import kr.hhplus.be.server.domain.order.model.OrderView
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    @Transactional
    fun getWithLockBy(orderCommand: OrderCommand.Order): OrderView {

        val order = orderRepository.findWithLockBy(orderCommand.orderId)
            ?: throw BusinessException(BusinessErrorCode.ORDER_NOT_EXIST)
        order.validateModifiable()
        return OrderView.from(order)
    }


    fun getAllActiveOrderProductsBy(orderCommand: OrderCommand.Order): List<OrderProductView> {

        val orderProducts = orderRepository.findAllActiveOrderProductsBy(orderCommand.orderId)
            ?: throw BusinessException(BusinessErrorCode.ORDER_PRODUCT_NOT_EXIST)
        return orderProducts.map { OrderProductView.from(it) }
    }

    @Transactional
    fun order(
        placeOrder: OrderCommand.PlaceOrder,
        placeOrderProductCommands: List<OrderCommand.PlaceOrderProduct>
    ): OrderView {

        val order = orderRepository.save(
            Order(
                userId = placeOrder.userId,
                userCouponId = placeOrder.userCouponId,
                status = placeOrder.status,
            )
        )

        val orderProducts = placeOrderProductCommands.map {
            it.copy(orderId = order.id)
        }

        orderRepository.saveAllOrderProducts(orderProducts.map {
            OrderProduct(
                productOptionId = it.productOptionId,
                orderId = it.orderId,
                productPrice = it.productPrice,
                quantity = it.quantity,
                productId = it.productId,
            )
        })
        return OrderView.from(order)
    }

    @Transactional
    fun modifyStatus(modifyOrderCommand: OrderCommand.ModifyStatus): OrderView {

        val order = orderRepository.findWithLockBy(modifyOrderCommand.orderId)
            ?: throw BusinessException(BusinessErrorCode.ORDER_NOT_EXIST)
        order.modifyStatusTo(modifyOrderCommand.status)

        val modifiedOrder = orderRepository.save(order)
        return OrderView.from(modifiedOrder)
    }
}