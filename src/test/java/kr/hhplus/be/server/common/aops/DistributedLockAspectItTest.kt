package kr.hhplus.be.server.common.aops

import junit.framework.TestCase.assertFalse
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class DistributedLockAspectItTest @Autowired constructor(
    private val lockTestService: LockTestService,
) : IntegrationTestSupport() {


    @Nested
    inner class UnLock {

        @Test
        fun `로직 실행 완료 후 락 해제에 성공한다`() {

            //given
            val testId = 1L

            //when
            lockTestService.doSomething(LockRequest(testId = testId))

            //then
            val lock = redissonClient.getLock("lock:test:$testId")
            assertFalse(lock.isLocked)
        }

        @Test
        fun `로직 실행 시 예외가 발생 할 경우 락 해제에 성공한다`() {

            //given
            val testId = 2L

            //when
            val exception = assertThrows<IllegalStateException> {
                lockTestService.throwing(LockRequest(testId = testId))
            }

            //then
            val lock = redissonClient.getLock("lock:test:$testId")
            assertFalse(lock.isLocked)
            assertThat(exception).isInstanceOf(IllegalStateException::class.java)
        }

    }
}