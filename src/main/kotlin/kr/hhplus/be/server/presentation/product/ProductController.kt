package kr.hhplus.be.server.presentation.product

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.product.model.ProductResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController {


    @Operation(
        summary = "상품 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "상품 조회"
            )
        ]
    )
    @GetMapping("{productId}")
    @SuccessResponse
    fun product(@PathVariable productId: Long): ProductResponse.Product =
        ProductResponse.Product(productId, "테스트 상품", 10_000L, 100L, "대")

}
