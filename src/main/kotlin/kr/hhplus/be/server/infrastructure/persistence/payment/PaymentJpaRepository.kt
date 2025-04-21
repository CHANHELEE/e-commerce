package kr.hhplus.be.server.infrastructure.persistence.payment

import kr.hhplus.be.server.infrastructure.persistence.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentJpaRepository : JpaRepository<PaymentEntity, Long> {
}