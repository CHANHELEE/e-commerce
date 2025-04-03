package kr.hhplus.be.server.presentation.order.model

class OrderRequest {

    data class Order(
        val userId: Long,
        val orderContents: List<OrderContent>
    )

    data class OrderContent(
        val productId: Long,
        val quantity: Long,
        val couponId: Long?,
    )

    data class Payment(
        val userId: Long,
        val orderId: Long,
    )
}