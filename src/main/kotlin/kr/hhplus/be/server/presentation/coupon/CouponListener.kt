package kr.hhplus.be.server.presentation.coupon

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.presentation.coupon.model.CouponIssuePayload
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class CouponListener(
    private val couponService: CouponService,
    private val objectMapper: ObjectMapper
) {


    @KafkaListener(
        topics = ["couponIssue"],
        groupId = "coupon",
    )
    fun listenToCouponIssue(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        try {
            val payload = objectMapper.readValue<CouponIssuePayload>(record.value())

            couponService.issue(
                CouponCommand.Issue(
                    couponId = record.key().toLong(),
                    userId = payload.userId,
                    requestId = payload.requestId
                )
            )
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } finally {
            acknowledgment.acknowledge()
        }
    }
}