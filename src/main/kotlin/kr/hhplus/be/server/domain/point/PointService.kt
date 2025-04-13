package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import kr.hhplus.be.server.domain.point.model.Point
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.point.model.PointHistory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    private val pointRepository: PointRepository
) {

    @Transactional
    fun charge(pointCommand: PointCommand.Charge): Point {

        var point = pointRepository.findUserPointWithLockBy(pointCommand.userId)
            ?: throw BusinessException(BusinessErrorCode.USER_POINT_NOT_FOUND)
        point.charge(pointCommand.amount)

        point = pointRepository.savePoint(point)

        val pointHistory =
            PointHistory(
                pointId = point.id!!,
                point = pointCommand.amount,
                type = PointHistoryType.CHARGE,
                createdAt = point.createdAt
            )
        pointRepository.savePointHistory(pointHistory)
        return point
    }

    fun getPoint(pointCommand: PointCommand.Point): Point =
        pointRepository.findBy(pointCommand.userId)
            ?: throw BusinessException(BusinessErrorCode.USER_POINT_NOT_FOUND)

    @Transactional
    fun usePoint(pointCommand: PointCommand.Update): Point {

        var userPoint = pointRepository.findUserPointWithLockBy(pointCommand.userId)
            ?: throw BusinessException(BusinessErrorCode.USER_POINT_NOT_FOUND)
        userPoint.use(pointCommand.amount)
        userPoint = pointRepository.updatePoint(userPoint)

        val pointHistory =
            PointHistory(
                pointId = userPoint.id!!,
                point = pointCommand.amount,
                type = PointHistoryType.USE,
                createdAt = userPoint.createdAt
            )
        pointRepository.savePointHistory(pointHistory)
        return userPoint
    }
}