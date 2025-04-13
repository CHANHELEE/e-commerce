package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import kr.hhplus.be.server.domain.point.model.Point
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.fixtures.point.PointChargeCommandFixture
import kr.hhplus.be.server.fixtures.point.PointCommandFixture
import kr.hhplus.be.server.fixtures.point.PointFixture
import kr.hhplus.be.server.fixtures.point.PointHistoryFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.then
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PointServiceTest {

    @InjectMocks
    private lateinit var pointService: PointService

    @Mock
    private lateinit var pointRepository: PointRepository

    @Test
    fun `포인트 충전에 성공한다`() {

        //given
        val pointChargeCommand = PointChargeCommandFixture.get()
        val point = PointFixture.get()
        val savedPoint = PointFixture.get(point = point.point + pointChargeCommand.amount)
        val pointHistory = PointHistoryFixture.get()
        given(pointRepository.findUserPointWithLockBy(pointChargeCommand.userId)).willReturn(point)
        given(pointRepository.savePoint(any())).willReturn(savedPoint)
        given(pointRepository.savePointHistory(any())).willReturn(pointHistory)

        //when
        val returnedPoint = pointService.charge(pointChargeCommand)


        //then
        assertThat(returnedPoint)
            .extracting("id", "point")
            .contains(savedPoint.id, savedPoint.point)
        verify(pointRepository, times(1)).findUserPointWithLockBy(pointChargeCommand.userId)
        verify(pointRepository, times(1)).savePoint(any())
        verify(pointRepository, times(1)).savePointHistory(any())
    }

    @Test
    fun `포인트 조회에 성공한다`() {

        //given
        val point = PointFixture.get()
        val pointCommandFixture = PointCommandFixture.get()
        given(pointRepository.findBy(any())).willReturn(point)

        //when
        val returnedPoint = pointService.getPoint(pointCommandFixture)


        //then
        assertThat(returnedPoint)
            .extracting("id", "point")
            .contains(point.id, point.point)
        verify(pointRepository, times(1)).findBy(any())
    }

    @Nested
    inner class UsePoint {

        @Test
        fun `사용자가 존재하고 포인트가 충분하면 정상 차감되고 이력 저장된다`() {
            // given
            val userId = 1L
            val useAmount = 500L
            val point = Point(id = 1L, userId = userId, point = 1000L, createdAt = LocalDateTime.now())
            val command = PointCommand.Update(userId, useAmount)

            given(pointRepository.findUserPointWithLockBy(userId)).willReturn(point)
            given(pointRepository.updatePoint(any())).willReturn(point)

            // when
            val result = pointService.usePoint(command)

            // then
            then(pointRepository).should().updatePoint(check {
                assertThat(it.point).isEqualTo(500L)
            })

            then(pointRepository).should().savePointHistory(check {
                assertThat(it.point).isEqualTo(useAmount)
                assertThat(it.pointId).isEqualTo(point.id)
                assertThat(it.type).isEqualTo(PointHistoryType.USE)
            })

            assertThat(result.point).isEqualTo(500L)
        }

        @Test
        fun `사용자 포인트가 존재하지 않으면 예외가 발생한다`() {
            // given
            val command = PointCommand.Update(userId = 1L, amount = 100L)
            given(pointRepository.findUserPointWithLockBy(1L)).willReturn(null)

            // when & then
            val exception = assertThrows<BusinessException> {
                pointService.usePoint(command)
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.USER_POINT_NOT_FOUND)
        }

        @Test
        fun `포인트가 부족하면 예외가 발생한다`() {
            // given
            val userId = 1L
            val point = Point(id = 1L, userId = userId, point = 100L)
            val command = PointCommand.Update(userId, amount = 200L)

            given(pointRepository.findUserPointWithLockBy(userId)).willReturn(point)

            // when & then
            val exception = assertThrows<BusinessException> {
                pointService.usePoint(command)
            }
            assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.POINT_NOT_ENOUGH)
        }
    }
}