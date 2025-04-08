package kr.hhplus.be.server.fixtures.point

import kr.hhplus.be.server.domain.point.model.PointCommand

object PointChargeCommandFixture {

    fun get(
        userId: Long = 1L,
        amount: Long = 10_000L,
    ): PointCommand.Charge = PointCommand.Charge(
        userId = userId,
        amount = amount,
    )
}