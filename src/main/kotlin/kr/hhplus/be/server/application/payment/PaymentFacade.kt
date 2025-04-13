package kr.hhplus.be.server.application.payment

import kr.hhplus.be.server.application.payment.model.PaymentCriteria
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.payment.model.Payment
import kr.hhplus.be.server.domain.payment.model.PaymentCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.ProductCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentFacade(
    private val productService: ProductService,
    private val couponService: CouponService,
    private val pointService: PointService,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
) {


    @Transactional
    fun pay(paymentCriteria: PaymentCriteria.PlacePayment): Payment {

        val order = orderService.getWithLockBy(OrderCommand.Order(paymentCriteria.orderId))

        val coupon = order.userCouponId?.let {
            var userCoupon = couponService.getUserCouponWithLockBy(CouponCommand.UserCoupon(order.userId, it))
            userCoupon.use()
            userCoupon = couponService.updateUserCoupon(CouponCommand.UseCoupon(userCoupon.id, userCoupon.usedAt!!))
            couponService.getCouponBy(CouponCommand.Coupon(userCoupon.couponId))
        }

        var originTotalPrice = 0L
        orderService.getAllActiveOrderProductsBy(OrderCommand.Order(order.id)).forEach {

            val stock = productService.getProductStockWithLockBy(
                ProductCommand.ProductStock(
                    productId = it.productId,
                    optionId = it.productOptionId
                )
            )
            stock.decreaseStock(it.quantity)
            productService.updateStock(ProductCommand.UpdateStock(stock.id, stock.stock))

            val product = productService.getProductBy(ProductCommand.Product(it.productId))
            originTotalPrice += product.price * it.quantity
        }

        val payTotalPrice = originTotalPrice - (coupon?.discountPrice ?: 0L)
        pointService.usePoint(PointCommand.Update(order.userId, payTotalPrice))

        val payment = paymentService.save(
            PaymentCommand.PlacePayment(
                orderId = order.id,
                originTotalPrice = originTotalPrice,
                payTotalPrice = payTotalPrice,
                discountPrice = coupon?.discountPrice,
            )
        )
        return payment
    }
}