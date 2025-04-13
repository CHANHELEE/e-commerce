package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.payment.model.Payment
import kr.hhplus.be.server.domain.payment.model.PaymentCommand
import kr.hhplus.be.server.domain.payment.model.PaymentHistory
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    fun save(paymentCommand: PaymentCommand.PlacePayment): Payment {

        val payment = paymentRepository.save(
            Payment(
                orderId = paymentCommand.orderId,
                originTotalPrice = paymentCommand.originTotalPrice,
                payTotalPrice = paymentCommand.payTotalPrice,
                discountPrice = paymentCommand.discountPrice
            )
        )

        paymentRepository.saveHistory(
            PaymentHistory(
                paymentId = payment.id,
                originTotalPrice = payment.originTotalPrice,
                payTotalPrice = paymentCommand.payTotalPrice,
                discountPrice = paymentCommand.discountPrice
            )
        )
        return payment
    }
}