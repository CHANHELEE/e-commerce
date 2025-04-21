package kr.hhplus.be.server.presentation.payment

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import kr.hhplus.be.server.application.payment.PaymentFacade
import kr.hhplus.be.server.application.payment.model.PaymentCriteria
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.payment.model.PaymentRequest
import kr.hhplus.be.server.presentation.payment.model.PaymentResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentFacade: PaymentFacade,
) {


    @Operation(
        summary = "주문 결제", responses = [SwaggerApiResponse(
            responseCode = "200", description = "주문 결제"
        )]
    )
    @PostMapping("")
    @SuccessResponse
    fun pay(
        @RequestBody @Valid request: PaymentRequest.Payment,
    ): PaymentResponse.Payment {
        val payment = paymentFacade.pay(
            PaymentCriteria.PlacePayment(
                request.orderId!!,
            )
        )
        return PaymentResponse.Payment(
            payment.id
        )
    }

}
