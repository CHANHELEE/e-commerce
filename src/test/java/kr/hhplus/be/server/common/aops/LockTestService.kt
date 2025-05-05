package kr.hhplus.be.server.common.aops

import kr.hhplus.be.server.common.annotations.DistributedLock
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class LockTestService {

    @DistributedLock(prefix = "test", key = "testId", waitTime = 1, leaseTime = 5)
    @Transactional
    fun doSomething(request: LockRequest) {
        println("doSomething")
    }

    @DistributedLock(prefix = "test", key = "testId", waitTime = 1, leaseTime = 5)
    @Transactional
    fun throwing(request: LockRequest) {
        throw IllegalStateException("forced failure")
    }
}

data class LockRequest(val testId: Long)
