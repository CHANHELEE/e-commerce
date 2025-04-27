package kr.hhplus.be.server.infrastructure.persistence.order

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository : JpaRepository<OrderEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(orderId: Long): OrderEntity?
}