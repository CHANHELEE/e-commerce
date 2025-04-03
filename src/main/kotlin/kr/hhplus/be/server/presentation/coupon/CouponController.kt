package kr.hhplus.be.server.presentation.coupon

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.coupon.model.CouponRequest
import kr.hhplus.be.server.presentation.coupon.model.CouponResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/coupons")
class CouponController {


    @Operation(
        summary = "쿠폰 목록 조회", responses = [SwaggerApiResponse(
            responseCode = "200", description = "쿠폰 목록 조회"
        )]
    )
    @GetMapping("")
    @SuccessResponse
    fun coupons(): List<CouponResponse.Coupon> =
        listOf(CouponResponse.Coupon(1L, 100L, "test_coupon"))

    @Operation(
        summary = "쿠폰 발급", responses = [SwaggerApiResponse(
            responseCode = "200", description = "쿠폰 목록 발급"
        )]
    )
    @PostMapping("")
    @SuccessResponse
    fun issue(
        @RequestBody request: CouponRequest.Issue,
    ): CouponResponse.Issue =
        CouponResponse.Issue(request.userId, request.couponId, "test_coupon")

}
