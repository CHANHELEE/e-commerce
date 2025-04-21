package kr.hhplus.be.server.infrastructure.persistence.payment

import kr.hhplus.be.server.infrastructure.persistence.payment.entity.PaymentHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentHistoryJpaRepository : JpaRepository<PaymentHistoryEntity, Long> {
}