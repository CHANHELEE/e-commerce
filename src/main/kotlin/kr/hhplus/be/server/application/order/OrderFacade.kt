package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.order.model.OrderCriteria
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.order.model.OrderView
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
    fun placeOrder(orderCriteria: OrderCriteria.PlaceOrder): OrderView {

        val userCoupon = orderCriteria.couponId?.let {
            couponService.validateUse(CouponCommand.UserCoupon(orderCriteria.userId, it))
        }

        pointService.validateUsable(PointCommand.Point(orderCriteria.userId))

        val placeOrderProductCommands: List<OrderCommand.PlaceOrderProduct> =
            orderCriteria.orderedProduct!!.map { orderedProduct ->

                productService.validateStock(
                    ProductCommand.ProductStock(
                        orderedProduct.productId,
                        orderedProduct.productOptionId
                    )
                )

                val product = productService.getBy(ProductCommand.Product(orderedProduct.productId))

                OrderCommand.PlaceOrderProduct(
                    productOptionId = orderedProduct.productOptionId,
                    orderId = 0L,
                    productPrice = product.price,
                    quantity = orderedProduct.quantity,
                    productId = orderedProduct.productId,
                )
            }

        val order = orderService.order(
            OrderCommand.PlaceOrder(
                orderCriteria.userId,
                userCoupon?.id,
                OrderStatus.PENDING
            ), placeOrderProductCommands
        )
        return order
    }
}