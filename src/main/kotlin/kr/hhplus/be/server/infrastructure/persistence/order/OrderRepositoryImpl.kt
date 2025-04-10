package kr.hhplus.be.server.infrastructure.persistence.order


import kr.hhplus.be.server.domain.order.OrderRepository
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderHistory
import kr.hhplus.be.server.domain.order.model.OrderProduct
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl : OrderRepository {

    override fun save(order: Order): Order {
        TODO("Not yet implemented")
    }

    override fun saveHistory(orderHistory: OrderHistory): OrderHistory {
        TODO("Not yet implemented")
    }

    override fun saveAllOrderProducts(orderProducts: List<OrderProduct>): Boolean {
        TODO("Not yet implemented")
    }

    override fun findWithLockBy(orderId: Long): Order? {
        TODO("Not yet implemented")
    }

    override fun findAllActiveOrderProductsBy(orderId: Long): List<OrderProduct>? {
        TODO("Not yet implemented")
    }
}