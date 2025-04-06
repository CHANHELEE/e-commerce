package kr.hhplus.be.server.presentation.statistics

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.statistics.model.StatisticResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/statistic")
class StatisticController {


    @Operation(
        summary = "가장 많이 팔린 상위 5개 상품 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "가장 많이 팔린 상위 5개 상품 조회"
            )
        ]
    )
    @GetMapping("/ranks/top-five")
    @SuccessResponse
    fun ranks(): List<StatisticResponse.TopFive> =
        listOf(
            StatisticResponse.TopFive("테스트 상품1", 1),
            StatisticResponse.TopFive("테스트 상품2", 2),
            StatisticResponse.TopFive("테스트 상품3", 3),
            StatisticResponse.TopFive("테스트 상품4", 4),
            StatisticResponse.TopFive("테스트 상품5", 5),
        )

}
