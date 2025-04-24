package kr.hhplus.be.server.presentation.coupon

import io.swagger.v3.oas.annotations.Operation
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.coupon.model.CouponRequest
import kr.hhplus.be.server.presentation.coupon.model.CouponResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/coupons")
class CouponController(
    private val couponService: CouponService,
) {


    @Operation(
        summary = "특정 쿠폰 조회", responses = [SwaggerApiResponse(
            responseCode = "200", description = "특정 쿠폰 조회"
        )]
    )
    @GetMapping("{couponId}")
    @SuccessResponse
    fun coupons(@PathVariable couponId: Long): CouponResponse.Coupon {

        val coupon = couponService.getCouponBy(CouponCommand.Coupon(couponId))
        return CouponResponse.Coupon(coupon.id, coupon.amount, coupon.name, coupon.discountPrice)
    }


    @Operation(
        summary = "쿠폰 발급", responses = [SwaggerApiResponse(
            responseCode = "200", description = "쿠폰 목록 발급"
        )]
    )
    @PostMapping("")
    @SuccessResponse
    fun issue(
        @RequestBody request: CouponRequest.Issue,
    ): CouponResponse.Issue {

        val userCoupon = couponService.issue(CouponCommand.Issue(request.couponId, request.userId))
        return CouponResponse.Issue(userCoupon.userId, userCoupon.couponId)
    }

}
