package kr.hhplus.be.server.presentation.statistics

import io.swagger.v3.oas.annotations.Operation
import kr.hhplus.be.server.domain.statistics.product.ProductStatisticService
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.statistics.model.StatisticResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/statistic")
class StatisticController(
    private val productStaticService: ProductStatisticService,
) {


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
    fun ranks(): List<StatisticResponse.TopFive> {
        val products = productStaticService.getAllPopularProducts()
        return products.map {
            StatisticResponse.TopFive(
                productId = it.productId,
                productName = it.productName,
                rank = it.rank
            )
        }
    }
}
