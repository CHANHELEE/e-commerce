package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderHistory
import kr.hhplus.be.server.domain.order.model.OrderProduct


interface OrderRepository {

    fun save(order: Order): Order

    fun saveHistory(orderHistory: OrderHistory): OrderHistory

    fun saveAllOrderProducts(orderProducts: List<OrderProduct>): Boolean
}