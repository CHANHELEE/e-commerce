package kr.hhplus.be.server.presentation.order.model

class OrderResponse {

    data class Order(
        val id: Long,
        val userId: Long,
    )

    data class OrderedProduct(
        val productId: Long,
        val productOptionId: Long,
        val quantity: Long,
    )
}