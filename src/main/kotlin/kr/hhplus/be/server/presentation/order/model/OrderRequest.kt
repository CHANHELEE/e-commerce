package kr.hhplus.be.server.presentation.order.model

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

class OrderRequest {

    data class NewOrder(
        @field:NotNull(message = "사용자 식별 값(userId)은 필수입니다.")
        val userId: Long?,
        val couponId: Long?,
        @field:NotEmpty(message = "주문 상품 목록(orderedProduct)은 필수입니다.")
        @field:Valid
        val orderedProduct: List<OrderedProduct>?,
    )

    data class OrderedProduct(
        @field:NotNull(message = "상품 식별 값(productId)은 필수입니다.")
        val productId: Long?,
        @field:NotNull(message = "상품 옵션 식별 값(productOptionId)은 필수입니다.")
        val productOptionId: Long?,
        @field:NotNull(message = "상품 수량 값(quantity)은 필수입니다.")
        val quantity: Long?,
    )

    data class Payment(
        val userId: Long,
        val orderId: Long,
    )
}