package kr.hhplus.be.server.presentation.order

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.order.model.OrderRequest
import kr.hhplus.be.server.presentation.order.model.OrderResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController {


    @Operation(
        summary = "주문 생성", responses = [
            SwaggerApiResponse(
                responseCode = "200", description = "주문 생성"
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
    @PostMapping("")
    @SuccessResponse
    fun order(@RequestBody @Valid orderRequest: OrderRequest.NewOrder): OrderResponse.Order =
        OrderResponse.Order(
            1L,
            null,
            listOf(OrderResponse.OrderedProduct(1L, 1L, 20L))
        )

    @Operation(
        summary = "주문 수정", responses = [
            SwaggerApiResponse(
                responseCode = "200", description = "주문 수정"
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
    @PatchMapping("")
    @SuccessResponse
    fun order(@RequestBody @Valid orderRequest: OrderRequest.PatchOrder): OrderResponse.Order =
        OrderResponse.Order(
            1L,
            1L,
            listOf(OrderResponse.OrderedProduct(1L, 1L, 20L))
        )



    @Operation(
        summary = "주문 결제", responses = [SwaggerApiResponse(
            responseCode = "200", description = "주문 결제"
        )]
    )
    @PostMapping("/{orderId}/payment")
    @SuccessResponse
    fun pay(
        @RequestBody request: OrderRequest.Payment,
    ): OrderResponse.Payment =
        OrderResponse.Payment(1L, 1L)

}
