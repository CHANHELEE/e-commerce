package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.order.enums.OrderStatus

class OrderCommand {

    data class PlaceOrder(
        val userId: Long,
        val userCouponId: Long?,
        val status: OrderStatus,
    )

    data class PlaceOrderHistory(
        val orderId: Long,
        val status: OrderStatus,
    )

    data class PlaceOrderProduct(
        var productOptionId: Long,
        var productId: Long,
        var orderId: Long,
        var productPrice: Long,
        var quantity: Long,
    )

    data class Order(
        val orderId: Long,
    )
}