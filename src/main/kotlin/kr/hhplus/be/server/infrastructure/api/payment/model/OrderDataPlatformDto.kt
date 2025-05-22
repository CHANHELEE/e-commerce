package kr.hhplus.be.server.infrastructure.api.payment.model

class OrderDataPlatformDto {

    data class PaymentSuccess(
        val paymentId: Long,
        val userId: Long,
        val productIdToQuantity: Map<Long, Long>,
    )
}