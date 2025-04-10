package kr.hhplus.be.server.application.order.model

class OrderCriteria {

    data class PlaceOrder(
        val userId: Long,
        val couponId: Long?,
        val orderedProduct: List<OrderedProduct>,
    )

    data class OrderedProduct(
        val productId: Long,
        val productOptionId: Long,
        val quantity: Long,
    )
}