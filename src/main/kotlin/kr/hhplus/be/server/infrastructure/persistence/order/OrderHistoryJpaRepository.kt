package kr.hhplus.be.server.infrastructure.persistence.order

import kr.hhplus.be.server.infrastructure.persistence.order.model.entity.OrderHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderHistoryJpaRepository : JpaRepository<OrderHistoryEntity, Long> {
}