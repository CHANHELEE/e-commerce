package kr.hhplus.be.server.infrastructure.api.payment

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.infrastructure.api.payment.model.OrderDataPlatformDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderDataPlatformClient(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val objectMapper: ObjectMapper,
) {

    fun sendSuccessOrder(payload: OrderDataPlatformDto.PaymentSuccess) {
        kafkaTemplate.send(
            "orderDataPlatform",
            payload.paymentId.toString(),
            objectMapper.writeValueAsString(payload)
        )
    }
}