package kr.hhplus.be.server.domain.payment.model

class PaymentCommand {

    data class PlacePayment(
        val orderId: Long,
        val originTotalPrice: Long,
        val payTotalPrice: Long,
        val discountPrice: Long? = null,
    )
}