package kr.hhplus.be.server.infrastructure.persistence.order


import kr.hhplus.be.server.domain.order.OrderRepository
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderHistory
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderHistoryEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderProductEntity
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderHistoryJpaRepository: OrderHistoryJpaRepository,
    private val orderProductJpaRepository: OrderProductJpaRepository,
) : OrderRepository {

    override fun save(order: Order): Order {
        return orderJpaRepository.save(OrderEntity.from(order)).toDomain()
    }

    override fun saveHistory(orderHistory: OrderHistory): OrderHistory {
        return orderHistoryJpaRepository.save(OrderHistoryEntity.from(orderHistory)).toDomain()
    }

    override fun saveAllOrderProducts(orderProducts: List<OrderProduct>) {
        orderProductJpaRepository.saveAll(OrderProductEntity.from(orderProducts))
    }

    override fun findWithLockBy(orderId: Long): Order? {
        return orderJpaRepository.findWithLockById(orderId)?.toDomain()
    }

    override fun findAllActiveOrderProductsBy(orderId: Long): List<OrderProduct>? {
        val activeOrderProducts = orderProductJpaRepository.findAllByOrderIdAndDeletedAtIsNotNull(orderId)
        return activeOrderProducts.map { it.toDomain() }
    }
}