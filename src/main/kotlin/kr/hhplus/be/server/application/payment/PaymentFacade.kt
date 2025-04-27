package kr.hhplus.be.server.application.payment

import kr.hhplus.be.server.application.payment.model.PaymentCriteria
import kr.hhplus.be.server.application.payment.model.PaymentTransactionalEvent
import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.model.OrderCommand
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus
import kr.hhplus.be.server.domain.payment.model.PaymentCommand
import kr.hhplus.be.server.domain.payment.model.PaymentView
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.ProductCommand
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentFacade(
    private val productService: ProductService,
    private val couponService: CouponService,
    private val pointService: PointService,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {


    @Transactional
    fun pay(paymentCriteria: PaymentCriteria.PlacePayment): PaymentView {

        val order = orderService.getWithLockBy(OrderCommand.Order(paymentCriteria.orderId))
        return try {

            val coupon = order.userCouponId?.let {
                val userCoupon = couponService.use(CouponCommand.UseCoupon(order.userCouponId))
                couponService.getCouponBy(CouponCommand.Coupon(userCoupon.couponId))
            }

            var originTotalPrice = 0L
            orderService.getAllActiveOrderProductsBy(OrderCommand.Order(order.id)).forEach {

                productService.decreaseStock(
                    ProductCommand.UpdateStock(
                        productId = it.productId,
                        optionId = it.productOptionId,
                        amount = it.quantity
                    )
                )

                val product = productService.getBy(ProductCommand.Product(it.productId))
                originTotalPrice += product.price * it.quantity
            }

            val payTotalPrice = originTotalPrice - (coupon?.discountPrice ?: 0L)
            pointService.use(PointCommand.Update(order.userId, payTotalPrice))

            val payment = paymentService.pay(
                PaymentCommand.PlacePayment(
                    orderId = order.id,
                    originTotalPrice = originTotalPrice,
                    payTotalPrice = payTotalPrice,
                    discountPrice = coupon?.discountPrice,
                    status = PaymentStatus.SUCCESS,
                )
            )
            payment
        } catch (ex: BusinessException) {

            applicationEventPublisher.publishEvent(
                PaymentTransactionalEvent.TransactionRollBackEvent(
                    orderId = order.id
                )
            )
            throw ex
        }
    }
}