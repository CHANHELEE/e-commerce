package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.order.model.OrderCriteria
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.ProductCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val productService: ProductService,
    private val couponService: CouponService,
    private val pointService: PointService,
    private val orderService: OrderService,
) {


    @Transactional
    fun placeOrder(orderCriteria: OrderCriteria.PlaceOrder): Order {

        val userCoupon = orderCriteria.couponId?.let {
            couponService.validateUse(CouponCommand.UserCoupon(orderCriteria.userId, it))
        }

        val point = pointService.getPoint(PointCommand.Point(orderCriteria.userId))
        point.validateUsable()

        val orderProducts: List<OrderCommand.PlaceOrderProduct> = orderCriteria.orderedProduct!!.map { orderedProduct ->
            val stock = productService.getProductStockBy(
                ProductCommand.ProductStock(
                    productId = orderedProduct.productId!!,
                    optionId = orderedProduct.productOptionId!!
                )
            )
            stock.validateStock()

            val product = productService.getProductBy(ProductCommand.Product(orderedProduct.productId))
            val price = product.price

            OrderCommand.PlaceOrderProduct(
                productOptionId = orderedProduct.productOptionId,
                orderId = 0L,
                productPrice = price,
                quantity = orderedProduct.quantity,
                productId = orderedProduct.productId,
            )
        }

        val order = orderService.save(
            OrderCommand.PlaceOrder(
                orderCriteria.userId,
                userCoupon?.id,
                OrderStatus.PENDING
            )
        )

        orderService.saveHistory(
            OrderCommand.PlaceOrderHistory(
                order.id,
                order.status,
            )
        )

        val orderProductsWithOrderId = orderProducts.map {
            it.copy(orderId = order.id)
        }
        orderService.saveOrderProducts(orderProductsWithOrderId)
        return order
    }
}