package kr.hhplus.be.server.common.aops

import kr.hhplus.be.server.common.annotations.DistributedLock
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
private class DistributedLockAspect(
    private val redissonClient: RedissonClient
) {

    @Around("@annotation(kr.hhplus.be.server.common.annotations.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any {

        val method = (joinPoint.signature as MethodSignature).method
        val lockAnnotation = method.getAnnotation(DistributedLock::class.java)
            ?: throw IllegalStateException("DistributedLock annotation not present")

        val keyFieldName = lockAnnotation.key

        val keyValue = joinPoint.args
            .firstNotNullOfOrNull { arg ->
                arg?.let {
                    val member = arg::class.members.firstOrNull { it.name == keyFieldName }
                    if (member is KProperty1<*, *>) {
                        @Suppress("UNCHECKED_CAST")
                        (member as KProperty1<Any, Any?>).get(arg)
                    } else {
                        null
                    }
                }
            } ?: throw IllegalStateException("Field '$keyFieldName' not found in any method argument")

        val lockKey = "lock:${lockAnnotation.prefix}:$keyValue"

        val lock = redissonClient.getLock(lockKey)
        val locked = lock.tryLock(
            lockAnnotation.waitTime,
            lockAnnotation.leaseTime,
            lockAnnotation.timeUnit
        )

        if (!locked) {
            throw RuntimeException("Failed to acquire lock for key: $lockKey")
        }

        try {
            val result = joinPoint.proceed()
            val isUnitReturn = method.returnType == Void.TYPE
            return if (isUnitReturn) {
                Unit
            } else {
                result
            }
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}
