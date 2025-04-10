package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderHistory
import kr.hhplus.be.server.domain.order.model.OrderProduct
import kr.hhplus.be.server.domain.payment.model.Payment
import kr.hhplus.be.server.domain.payment.model.PaymentHistory


interface PaymentRepository {

    fun save(payment: Payment): Payment

    fun saveHistory(paymentHistory: PaymentHistory): PaymentHistory
}