package kr.hhplus.be.server.infrastructure.api.payment

import kr.hhplus.be.server.infrastructure.api.payment.model.OrderDataPlatformDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class OrderDataPlatformClient(
    private val orderDataPlatformRestClient: RestClient,
) {

    fun sendSuccessOrder(body: OrderDataPlatformDto.PaymentSuccess) {
//        orderDataPlatformRestClient.post().uri("/mock").body(body).retrieve().toBodilessEntity()
    }
}