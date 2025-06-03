package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.event.CouponIssueFailedEvent
import kr.hhplus.be.server.domain.coupon.model.event.CouponIssuedSuccessEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponEventListener(
    private val couponIssueRequestRepository: CouponIssueRequestRepository
) {

    @Async
    @EventListener
    fun handle(event: CouponIssueFailedEvent) {
        couponIssueRequestRepository.saveResult(event.requestId, event.exception.errorCode.code)

        if (event.exception.errorCode.code == BusinessErrorCode.COUPON_OUT_OF_AMOUNT.code) {
            couponIssueRequestRepository.deleteAvailableCoupon(event.couponId)
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: CouponIssuedSuccessEvent) {
        couponIssueRequestRepository.saveResult(event.requestId, "SUCCESS")
    }
}