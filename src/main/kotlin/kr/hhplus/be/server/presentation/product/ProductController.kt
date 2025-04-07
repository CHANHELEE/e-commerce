package kr.hhplus.be.server.presentation.product

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.common.model.PagingRequest
import kr.hhplus.be.server.presentation.common.model.PagingResponse
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

    @Operation(
        summary = "상품 목록 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "상품 목록 조회"
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청 - 필수값 누락 또는 유효성 검사 실패",
                content = [
                    io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = io.swagger.v3.oas.annotations.media.Schema(implementation = kr.hhplus.be.server.presentation.common.ApiResponse::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("")
    @SuccessResponse
    fun products(@ModelAttribute @Valid pagingRequest: PagingRequest): PagingResponse<ProductResponse.Product> =
        PagingResponse(
            1,
            1,
            1,
            listOf( ProductResponse.Product(1L, "테스트 상품", 10_000L, 100L, "대"))
        )

}
