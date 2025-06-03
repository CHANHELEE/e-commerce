package kr.hhplus.be.server.domain.coupon

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.coupon.model.*
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import kr.hhplus.be.server.domain.coupon.model.event.CouponIssueFailedEvent
import kr.hhplus.be.server.domain.coupon.model.event.CouponIssuedSuccessEvent
import kr.hhplus.be.server.presentation.coupon.model.CouponIssuePayload
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponIssueRequestRepository: CouponIssueRequestRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val objectMapper: ObjectMapper,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    fun getUserCouponBy(couponCommand: CouponCommand.UserCoupon): UserCouponView =
        UserCouponView.from(
            couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
                ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        )

    fun getCouponBy(couponCommand: CouponCommand.Coupon): CouponView =
        CouponView.from(
            couponRepository.findCouponBy(couponCommand.couponId)
                ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        )


    @Transactional
    fun issue(couponCommand: CouponCommand.Issue): UserCouponView {

        couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
            ?.let { throw BusinessException(BusinessErrorCode.COUPON_ALREADY_ISSUED) }

        val coupon = couponRepository.findCouponWithLockBy(couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)

        try {
            coupon.issue()
            couponRepository.saveCoupon(coupon)

            val userCoupon =
                couponRepository.saveUserCoupon(UserCoupon(couponId = coupon.id, userId = couponCommand.userId))

            applicationEventPublisher.publishEvent(CouponIssuedSuccessEvent(couponCommand.requestId))
            return UserCouponView.from(userCoupon)
        } catch (e: BusinessException) {
            applicationEventPublisher.publishEvent(
                CouponIssueFailedEvent(
                    couponCommand.requestId,
                    couponCommand.couponId,
                    e
                )
            )
            throw e
        }
    }

    fun validateUse(couponCommand: CouponCommand.UserCoupon): UserCouponView {

        val userCoupon = couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        userCoupon.validateUsable()
        return UserCouponView.from(userCoupon)
    }

    @Transactional
    fun use(couponCommand: CouponCommand.UseCoupon): UserCouponView {

        var userCoupon = couponRepository.findUserCouponWithLockBy(couponCommand.userCouponId)
            ?: throw BusinessException(BusinessErrorCode.USER_COUPON_NOT_EXIST)
        userCoupon.use()
        userCoupon = couponRepository.saveUserCoupon(userCoupon)
        return UserCouponView.from(userCoupon)
    }

    fun requestIssue(couponCommand: CouponCommand.RequestIssue): CouponIssueRequestView {

        val isAvailable = couponIssueRequestRepository.isAvailableCoupon(couponCommand.couponId)
        if (!isAvailable) {
            throw BusinessException(BusinessErrorCode.COUPON_OUT_OF_AMOUNT)
        }

        val requestId = couponCommand.couponId.toString() + "-" + couponCommand.userId.toString()
        kafkaTemplate.send(
            "couponIssue",
            couponCommand.couponId.toString(),
            objectMapper.writeValueAsString(
                CouponIssuePayload.from(
                    couponCommand = couponCommand,
                    requestId = requestId
                )
            )
        )
        return CouponIssueRequestView(requestId = requestId)
    }

    fun checkIssued(requestId: String): CouponsIssueResult {
        return couponIssueRequestRepository.getResult(requestId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_ISSUE_RESULT_NOT_EXIST)
    }
}