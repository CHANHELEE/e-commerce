package kr.hhplus.be.server.application.payment.model

class PaymentCriteria {

    data class PlacePayment(
        val orderId: Long,
    )
}