package kr.hhplus.be.server.infrastructure.persistence.payment


import kr.hhplus.be.server.domain.payment.PaymentRepository
import kr.hhplus.be.server.domain.payment.model.entity.Payment
import kr.hhplus.be.server.domain.payment.model.entity.PaymentHistory
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl : PaymentRepository {
    override fun save(payment: Payment): Payment {
        TODO("Not yet implemented")
    }

    override fun saveHistory(paymentHistory: PaymentHistory): PaymentHistory {
        TODO("Not yet implemented")
    }
}