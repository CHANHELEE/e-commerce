package kr.hhplus.be.server.presentation.order

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.order.model.OrderRequest
import kr.hhplus.be.server.presentation.order.model.OrderResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController {


    @Operation(
        summary = "주문 생성", responses = [SwaggerApiResponse(
            responseCode = "200", description = "주문 생성"
        )]
    )
    @PostMapping("")
    @SuccessResponse
    fun order(@RequestBody orderRequest: OrderRequest.Order): OrderResponse.Order =
        OrderResponse.Order(
            1L,
            null,
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
