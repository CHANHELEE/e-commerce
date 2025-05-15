package kr.hhplus.be.server.infrastructure.persistence.order


import kr.hhplus.be.server.domain.order.OrderRepository
import kr.hhplus.be.server.domain.order.model.entity.Order
import kr.hhplus.be.server.domain.order.model.entity.OrderProduct
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderHistoryEntity
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderProductEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderHistoryJpaRepository: OrderHistoryJpaRepository,
    private val orderProductJpaRepository: OrderProductJpaRepository,
) : OrderRepository {

    @Transactional
    override fun save(order: Order): Order {
        val order = orderJpaRepository.save(OrderEntity.from(order))
        orderHistoryJpaRepository.save(OrderHistoryEntity.from(order))
        return order.toDomain()
    }

    override fun saveAllOrderProducts(orderProducts: List<OrderProduct>) {
        orderProductJpaRepository.saveAll(OrderProductEntity.from(orderProducts))
    }

    override fun findWithLockBy(orderId: Long): Order? {
        return orderJpaRepository.findWithLockById(orderId)?.toDomain()
    }

    override fun findAllActiveOrderProductsBy(orderId: Long): List<OrderProduct>? {
        val activeOrderProducts = orderProductJpaRepository.findAllByOrderIdAndDeletedAtIsNull(orderId)
        return activeOrderProducts.map { it.toDomain() }
    }
}