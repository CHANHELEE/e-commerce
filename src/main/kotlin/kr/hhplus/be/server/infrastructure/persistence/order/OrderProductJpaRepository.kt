package kr.hhplus.be.server.infrastructure.persistence.order

import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderProductJpaRepository : JpaRepository<OrderProductEntity, Long> {

    fun findAllByOrderIdAndDeletedAtIsNotNull(orderId: Long): List<OrderProductEntity>
}