package kr.hhplus.be.server.presentation.payment.model

import jakarta.validation.constraints.NotNull

class PaymentRequest {

    data class Payment(
        @field:NotNull(message = "주문 식별 값(orderId)은 필수입니다.")
        val orderId: Long?,
    )
}