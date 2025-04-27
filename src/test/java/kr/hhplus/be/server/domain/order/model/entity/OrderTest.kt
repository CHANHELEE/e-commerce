package kr.hhplus.be.server.domain.order.model.entity

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.order.enums.OrderStatus
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class OrderTest {

    @Test
    fun `쿠폰 변경 가능 여부 검증 - SUCCESS 상태면 BusinessException(ORDER_ALREADY_COMPLETED) 예외 발생`() {
        // given
        val order = Order(
            id = 1L,
            userId = 100L,
            status = OrderStatus.SUCCESS
        )

        // when & then
        val exception = assertThrows<BusinessException> {
            order.validateModifiable()
        }
        assertEquals(BusinessErrorCode.ORDER_ALREADY_COMPLETED, exception.errorCode)
    }

    @Test
    fun `쿠폰 변경 가능 여부 검증 -  SUCCESS 상태가 아니라면 예외 미발생`() {
        // given
        val order = Order(
            id = 2L,
            userId = 101L,
            status = OrderStatus.PENDING
        )

        // when & then
        assertDoesNotThrow {
            order.validateModifiable()
        }
    }

    @Test
    fun `주문 상태 변경 시 주문이 SUCCESS 상태 일 경우 상태 변경에 실패한다`() {

        //given
        val order = Order(
            id = 1L,
            userId = 100L,
            status = OrderStatus.SUCCESS
        )

        // when & then
        val exception = assertThrows<BusinessException> {
            order.modifyStatusTo(OrderStatus.FAIL)
        }
        assertEquals(BusinessErrorCode.ORDER_ALREADY_COMPLETED, exception.errorCode)
    }
}