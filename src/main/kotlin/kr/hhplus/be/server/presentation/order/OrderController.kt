package kr.hhplus.be.server.presentation.order

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.application.order.model.OrderCriteria
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.order.model.OrderRequest
import kr.hhplus.be.server.presentation.order.model.OrderResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderFacade: OrderFacade,
) {


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
    fun order(@RequestBody @Valid orderRequest: OrderRequest.NewOrder): OrderResponse.Order {
        val order = orderFacade.placeOrder(
            OrderCriteria.PlaceOrder(
                userId = orderRequest.userId!!,
                couponId = orderRequest.couponId,
                orderedProduct = orderRequest.orderedProduct!!.map {
                    OrderCriteria.OrderedProduct(
                        productId = it.productId!!,
                        productOptionId = it.productOptionId!!,
                        quantity = it.quantity!!
                    )
                }
            )
        )
        return OrderResponse.Order(order.id, order.userId)
    }

}
