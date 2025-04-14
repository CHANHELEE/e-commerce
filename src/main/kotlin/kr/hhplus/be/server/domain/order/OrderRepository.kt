package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
import java.time.LocalDateTime


interface OrderRepository {

    fun save(order: Order): Order

    fun saveHistory(orderHistory: OrderHistory): OrderHistory

    fun saveAllOrderProducts(orderProducts: List<OrderProduct>): Boolean

    fun findWithLockBy(orderId: Long): Order?
    
    fun findAllActiveOrderProductsBy(orderId: Long): List<OrderProduct>?

    fun findTop5BestProduct(from: LocalDateTime): List<OrderProduct>?
}