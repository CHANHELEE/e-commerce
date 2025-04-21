package kr.hhplus.be.server.presentation.point


import io.swagger.v3.oas.annotations.Operation
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.model.PointCommand
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.point.model.PointRequest
import kr.hhplus.be.server.presentation.point.model.PointResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService
) {


    @Operation(
        summary = "사용자의 보유 포인트 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "사용자의 보유 포인트를 조회"
            )
        ]
    )
    @GetMapping("")
    @SuccessResponse
    fun point(@RequestParam userId: Long): PointResponse.Point {

        val userPoint = pointService.get(PointCommand.Point(userId))
        return PointResponse.Point(
            point = userPoint.point,
            userId = userPoint.userId
        )
    }

    @Operation(
        summary = "사용자 포인트 충전",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "사용자 포인트 충전"
            )
        ]
    )
    @PatchMapping("")
    @SuccessResponse
    fun charge(@RequestBody request: PointRequest.Charge): PointResponse.Charge {

        val userPoint = pointService.charge(
            PointCommand.Charge(
                userId = request.userId,
                amount = request.point,
            )
        )

        return PointResponse.Charge(
            userId = userPoint.userId,
            point = userPoint.point
        )
    }
}
