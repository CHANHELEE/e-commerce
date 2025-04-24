package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.point.model.PointCommand
import kr.hhplus.be.server.infrastructure.persistence.point.entity.PointEntity
import kr.hhplus.be.server.infrastructure.persistence.user.entity.UserEntity
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class PointServiceItTest : IntegrationTestSupport() {

    @Autowired
    lateinit var pointService: PointService

    @Nested
    inner class Charge {

        @Test
        fun `포인트를 충전에 성공한다`() {

            //given
            val userId = userJpaRepository.save(
                UserEntity(
                    name = "user",
                )
            ).id
            val chargeAmount = 500L
            val point = pointJpaRepository.save(
                PointEntity(
                    userId = userId!!,
                    point = 10_000L,
                )
            )

            //when
            val result = pointService.charge(PointCommand.Charge(userId, chargeAmount))

            //then
            assertThat(result.point).isEqualTo(point.point + chargeAmount)
        }

        @Test
        fun `동시성 테스트 - 동시에 포인트 충전 시 누락 없이 누적되어야 한다`() {

            // given
            val userId = userJpaRepository.save(
                UserEntity(
                    name = "user",
                )
            ).id
            val chargePerThread = 100L
            val threadCount = 50

            val point = pointJpaRepository.save(
                PointEntity(
                    userId = userId!!,
                    point = 10_000L,
                )
            )

            val executorService = Executors.newFixedThreadPool(threadCount)
            val latch = CountDownLatch(threadCount)

            // when
            repeat(threadCount) {
                executorService.submit {
                    try {
                        pointService.charge(
                            PointCommand.Charge(
                                userId = userId,
                                amount = chargePerThread
                            )
                        )
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()

            // then
            val result = pointJpaRepository.findByUserId(userId)!!
            val totalCharge = chargePerThread * threadCount
            assertThat(result.point).isEqualTo(point.point + totalCharge)
        }
    }

    @Nested
    inner class Use {

        @Test
        fun `포인트를 사용에 성공한다`() {

            //given
            val userId = userJpaRepository.save(
                UserEntity(
                    name = "user",
                )
            ).id
            val useAmount = 1000L
            val point = pointJpaRepository.save(
                PointEntity(
                    userId = userId!!,
                    point = 10_000L,
                )
            )
            //when
            val result = pointService.use(PointCommand.Update(userId, useAmount))

            //then
            assertThat(result.point).isEqualTo(point.point - useAmount)
        }

        @Test
        fun `동시성 테스트 - 동시에 포인트 사용 시 누락이 없이 사용되어야 한다`() {
            // given
            val userId = userJpaRepository.save(
                UserEntity(
                    name = "user",
                )
            ).id
            val usePerThread = 1_000L
            val threadCount = 10

            val point = pointJpaRepository.save(
                PointEntity(
                    userId = userId!!,
                    point = 20_000L // 총 사용 가능한 포인트
                )
            )

            val executorService = Executors.newFixedThreadPool(threadCount)
            val latch = CountDownLatch(threadCount)

            // when
            repeat(threadCount) {
                executorService.submit {
                    try {
                        pointService.use(
                            PointCommand.Update(
                                userId = userId,
                                amount = usePerThread
                            )
                        )
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()

            // then
            val result = pointJpaRepository.findByUserId(userId)!!
            val totalUsed = usePerThread * threadCount

            assertThat(result.point).isEqualTo(point.point - totalUsed)
        }
    }

    @Test
    fun `동시성 테스트 - 동시에 포인트 사용 시 0원일 때 포인트 사용에 실패해야 한다`() {
        // given
        val userId = userJpaRepository.save(
            UserEntity(
                name = "user",
            )
        ).id
        val usePerThread = 1_000L
        val threadCount = 1000
        val successCount = AtomicInteger(0)

        val point = pointJpaRepository.save(
            PointEntity(
                userId = userId!!,
                point = 20_000L // 총 사용 가능한 포인트
            )
        )

        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        // when
        repeat(threadCount) {
            executorService.submit {
                try {
                    pointService.use(
                        PointCommand.Update(
                            userId = userId,
                            amount = usePerThread
                        )
                    )
                    successCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        assertThat(successCount.get()).isEqualTo(20)
    }
}
