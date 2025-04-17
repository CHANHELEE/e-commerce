package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.payment.model.entity.Payment
import kr.hhplus.be.server.domain.payment.model.PaymentCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PaymentServiceTest {

    @Mock
    lateinit var paymentRepository: PaymentRepository

    @InjectMocks
    lateinit var paymentService: PaymentService

    @Test
    fun `결제가 저장되고 결제 히스토리도 함께 저장된다`() {

        // given
        val command = PaymentCommand.PlacePayment(
            orderId = 1L,
            originTotalPrice = 10000L,
            payTotalPrice = 9000L,
            discountPrice = 1000L
        )

        val savedPayment = Payment(
            id = 1L,
            orderId = command.orderId,
            originTotalPrice = command.originTotalPrice,
            payTotalPrice = command.payTotalPrice,
            discountPrice = command.discountPrice,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(paymentRepository.save(any())).willReturn(savedPayment)

        // when
        paymentService.pay(command)

        // then
        then(paymentRepository).should().save(check {
            assertThat(it.orderId).isEqualTo(command.orderId)
            assertThat(it.payTotalPrice).isEqualTo(command.payTotalPrice)
        })

        then(paymentRepository).should().saveHistory(check {
            assertThat(it.paymentId).isEqualTo(savedPayment.id)
            assertThat(it.originTotalPrice).isEqualTo(command.originTotalPrice)
            assertThat(it.payTotalPrice).isEqualTo(command.payTotalPrice)
            assertThat(it.discountPrice).isEqualTo(command.discountPrice)
        })
    }
}