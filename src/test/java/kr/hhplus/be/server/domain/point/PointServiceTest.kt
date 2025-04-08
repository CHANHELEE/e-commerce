package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.fixtures.point.PointChargeCommandFixture
import kr.hhplus.be.server.fixtures.point.PointCommandFixture
import kr.hhplus.be.server.fixtures.point.PointFixture
import kr.hhplus.be.server.fixtures.point.PointHistoryFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

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
}