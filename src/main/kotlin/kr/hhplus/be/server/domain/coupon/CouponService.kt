package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.annotations.DistributedLock
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.common.model.DistributedLockKeys
import kr.hhplus.be.server.common.model.DistributedLockPrefixes
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.coupon.model.CouponIssueRequestView
import kr.hhplus.be.server.domain.coupon.model.CouponView
import kr.hhplus.be.server.domain.coupon.model.UserCouponView
import kr.hhplus.be.server.domain.coupon.model.entity.UserCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponIssueRequestRepository: CouponIssueRequestRepository
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

    @DistributedLock(
        key = DistributedLockKeys.ISSUE_COUPON,
        prefix = DistributedLockPrefixes.ISSUE_COUPON,
    )
    @Transactional
    fun issue(couponCommand: CouponCommand.Issue): UserCouponView {

        couponRepository.findUserCouponBy(couponCommand.userId, couponCommand.couponId)
            ?.let { throw BusinessException(BusinessErrorCode.COUPON_ALREADY_ISSUED) }

        val coupon = couponRepository.findCouponWithLockBy(couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        coupon.issue()
        couponRepository.saveCoupon(coupon)

        val userCoupon =
            couponRepository.saveUserCoupon(UserCoupon(couponId = coupon.id, userId = couponCommand.userId))
        return UserCouponView.from(userCoupon)
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

    fun requestIssue(couponCommand: CouponCommand.Issue): CouponIssueRequestView {

        val couponAmount = couponIssueRequestRepository.getCouponAmount(couponCommand.couponId)
            ?: throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        if (couponAmount <= 0) {
            throw BusinessException(BusinessErrorCode.COUPON_NOT_EXIST)
        }

        val new =
            couponIssueRequestRepository.saveRequestingUser(
                userId = couponCommand.userId,
                couponId = couponCommand.couponId
            )
        if (!new) {
            throw BusinessException(BusinessErrorCode.DUPLICATED_COUPON_ISSUE_REQUEST)
        }

        val decreasedCouponAmount = couponIssueRequestRepository.decreaseCouponAmount(couponCommand.couponId)
        if (decreasedCouponAmount < 0) {
            couponIssueRequestRepository.deleteCouponAmount(couponCommand.couponId)
            throw BusinessException(BusinessErrorCode.COUPON_OUT_OF_AMOUNT)
        }

        couponIssueRequestRepository.saveIssueRequest(couponCommand.userId, couponCommand.couponId)
        return CouponIssueRequestView(couponId = couponCommand.couponId, userId = couponCommand.userId)
    }
}