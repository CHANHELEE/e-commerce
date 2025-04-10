package kr.hhplus.be.server.presentation.payment

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.payment.model.PaymentRequest
import kr.hhplus.be.server.presentation.payment.model.PaymentResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payment")
class PaymentController {


    @Operation(
        summary = "주문 결제", responses = [SwaggerApiResponse(
            responseCode = "200", description = "주문 결제"
        )]
    )
    @PostMapping("")
    @SuccessResponse
    fun pay(
        @RequestBody @Valid request: PaymentRequest.Payment,
    ): PaymentResponse.Payment =
        PaymentResponse.Payment(1L)

}
