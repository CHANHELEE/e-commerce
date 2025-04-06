package kr.hhplus.be.server.presentation.order.model

class OrderRequest {

    data class Order(
        val userId: Long,
        val couponId: Long?,
        val orderedProduct: List<OrderedProduct>
    )

    data class OrderedProduct(
        val productId: Long,
        val productOptionId: Long,
        val quantity: Long,
    )

    data class Payment(
        val userId: Long,
        val orderId: Long,
    )
}