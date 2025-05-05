package kr.hhplus.be.server.common.annotations

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val key: String,
    val prefix: String,
    val waitTime: Long = 5,
    val leaseTime: Long = 20,
    val timeUnit: TimeUnit = TimeUnit.SECONDS
)

