package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct


interface OrderRepository {

    fun save(order: Order): Order

    fun saveHistory(orderHistory: OrderHistory): OrderHistory

    fun saveAllOrderProducts(orderProducts: List<OrderProduct>)

    fun findWithLockBy(orderId: Long): Order?

    fun findAllActiveOrderProductsBy(orderId: Long): List<OrderProduct>?
}