package kr.hhplus.be.server.presentation.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.user.model.UserResponse
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/users")
class UserController {


    @Operation(
        summary = "사용자의 보유 포인트 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "사용자의 보유 포인트를 조회"
            )
        ]
    )
    @GetMapping("{userId}/point")
    @SuccessResponse
    fun point(@PathVariable userId: Long): UserResponse.Point =
        UserResponse.Point(userId, 10_000L)

    @Operation(
        summary = "사용자 포인트 충전",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "사용자 포인트 충전"
            )
        ]
    )
    @PatchMapping("{userId}/point")
    @SuccessResponse
    fun charge(@PathVariable userId: Long, @RequestBody point: Long): UserResponse.Charge =
        UserResponse.Charge(userId, 10_000L)

    @Operation(
        summary = "사용자의 보유 쿠폰 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "사용자의 보유 쿠폰을 조회"
            )
        ]
    )
    @GetMapping("{userId}/coupons")
    @SuccessResponse
    fun coupons(@PathVariable userId: Long): List<UserResponse.Coupons> =
        listOf(
            UserResponse.Coupons(userId, 1L, "테스트 쿠폰1", LocalDateTime.now()),
            UserResponse.Coupons(userId, 2L, "테스트 쿠폰2"),
        )

}
