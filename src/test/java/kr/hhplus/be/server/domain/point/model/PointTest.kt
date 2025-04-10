package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.fixtures.point.PointFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows

class PointTest {

    @Nested
    inner class Charge {

        @Test
        fun `포인트 충전에 성공한다`() {

            //given
            val point = Point(userId = 1L, point = 10_000L)
            val beforeChargePoint = point.point
            val amount = 1_000L

            //when
            point.charge(amount)

            //then
            assertThat(point.point).isEqualTo(beforeChargePoint + amount)
        }

        @Test
        fun `포인트 충전시 최소값 미만으로 충전시 BusinessException(INVALID_POINT_CHARGE_AMOUNT) 예외가 발생한다`() {

            //given
            val point = Point(userId = 1L, point = 100L)
            val amount = Point.MIN - 1

            //when
            val exception = assertThrows<BusinessException> {
                point.charge(amount)
            }

            //then
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.INVALID_POINT_CHARGE_AMOUNT)
        }

        @Test
        fun `포인트 보유 기준을 초과 할 경우 BusinessException(EXCEED_POINT_LIMIT) 예외가 발생한다`() {

            //given
            val point = Point(userId = 1L, point = Point.MAX)
            val amount = 1L

            //when
            val exception = assertThrows<BusinessException> {
                point.charge(amount)
            }

            //then
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.EXCEED_POINT_LIMIT)
        }

    }

    @Test
    fun `사용시 포인트가 0이면 POINT_NOT_ENOUGH 예외가 발생한다`() {
        val point = PointFixture.get(point = 0)

        //when
        val exception = assertThrows<BusinessException> {
            point.validateUsable()
        }

        //then
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.POINT_NOT_ENOUGH)
    }
}