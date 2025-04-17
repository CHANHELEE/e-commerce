package kr.hhplus.be.server.infrastructure.persistence.order

import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository : JpaRepository<OrderEntity, Long> {

    fun findWithLockById(orderId: Long): OrderEntity?
}