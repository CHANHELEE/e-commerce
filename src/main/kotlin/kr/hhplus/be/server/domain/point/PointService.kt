package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import kr.hhplus.be.server.domain.point.enums.PointHistoryType
import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.domain.point.model.PointView
import kr.hhplus.be.server.domain.point.model.entity.PointHistory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    private val pointRepository: PointRepository
) {

    @Transactional
    fun charge(pointCommand: PointCommand.Charge): PointView {

        var userPoint = pointRepository.findUserPointWithLockBy(pointCommand.userId)
            ?: throw BusinessException(BusinessErrorCode.USER_POINT_NOT_FOUND)
        userPoint.charge(pointCommand.amount)

        userPoint = pointRepository.savePoint(userPoint)

        val pointHistory =
            PointHistory(
                pointId = userPoint.id!!,
                point = pointCommand.amount,
                type = PointHistoryType.CHARGE,
                createdAt = userPoint.createdAt
            )
        pointRepository.savePointHistory(pointHistory)
        return PointView.from(userPoint)
    }

    fun get(pointCommand: PointCommand.Point): PointView =
        PointView.from(
            pointRepository.findBy(pointCommand.userId)
                ?: throw BusinessException(BusinessErrorCode.USER_POINT_NOT_FOUND)
        )

    fun validateUsable(pointCommand: PointCommand.Point): PointView {
        val userPoint = pointRepository.findBy(pointCommand.userId)
            ?: throw BusinessException(BusinessErrorCode.USER_POINT_NOT_FOUND)
        userPoint.validateUsable()
        return PointView.from(userPoint)
    }

    @Transactional
    fun use(pointCommand: PointCommand.Update): PointView {

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
        return PointView.from(userPoint)
    }
}