package kr.hhplus.be.server.infrastructure.persistence.payment


import kr.hhplus.be.server.domain.payment.PaymentRepository
import kr.hhplus.be.server.domain.payment.model.entity.Payment
import kr.hhplus.be.server.domain.payment.model.entity.PaymentHistory
import kr.hhplus.be.server.infrastructure.persistence.payment.entity.PaymentEntity
import kr.hhplus.be.server.infrastructure.persistence.payment.entity.PaymentHistoryEntity
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val paymentJpaRepository: PaymentJpaRepository,
    private val paymentHistoryJpaRepository: PaymentHistoryJpaRepository,
) : PaymentRepository {
    override fun save(payment: Payment): Payment {
        return paymentJpaRepository.save(PaymentEntity.from(payment)).toDomain()
    }

    override fun saveHistory(paymentHistory: PaymentHistory): PaymentHistory {
        return paymentHistoryJpaRepository.save(PaymentHistoryEntity.from(paymentHistory)).toDomain()
    }
}