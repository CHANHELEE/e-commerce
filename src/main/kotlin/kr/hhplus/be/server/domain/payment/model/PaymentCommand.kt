package kr.hhplus.be.server.domain.payment.model

import kr.hhplus.be.server.domain.payment.enums.PaymentStatus

class PaymentCommand {

    data class PlacePayment(
        val orderId: Long,
        val originTotalPrice: Long,
        val payTotalPrice: Long,
        val discountPrice: Long? = null,
        val status: PaymentStatus,
    )
}