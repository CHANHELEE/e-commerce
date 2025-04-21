package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.payment.model.entity.Payment
import kr.hhplus.be.server.domain.payment.model.entity.PaymentHistory


interface PaymentRepository {

    fun save(payment: Payment): Payment

    fun saveHistory(paymentHistory: PaymentHistory): PaymentHistory
}