package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponCommand
import kr.hhplus.be.server.infrastructure.persistence.coupon.redis.CouponIssueKeyPrefix
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisKeyCommands
import org.springframework.data.redis.core.ScanOptions

@ExtendWith(MockitoExtension::class)
class CouponSchedulerTest {

    @Mock
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    lateinit var couponService: CouponService

    @InjectMocks
    lateinit var couponScheduler: CouponScheduler

    @Test
    fun `쿠폰 발급 요청 큐에 유저가 있으면 RDB 적재를 시도한다`() {
        // given
        val couponId = 123L
        val userId = 456L
        val key = "${CouponIssueKeyPrefix.ISSUE_TARGET.prefix}$couponId"
        val rawKey = key.toByteArray()
        val cursor = getTestCursor(rawKey)

        val keyCommands = mock(RedisKeyCommands::class.java)
        whenever(keyCommands.scan(any<ScanOptions>())).thenReturn(cursor)

        val redisConnection = mock(RedisConnection::class.java)
        whenever(redisConnection.keyCommands()).thenReturn(keyCommands)

        val callbackCaptor = argumentCaptor<RedisCallback<RedisKeyCommands>>()
        whenever(redisTemplate.execute(callbackCaptor.capture())).thenAnswer {
            val callback = callbackCaptor.firstValue
            callback.doInRedis(redisConnection)
        }

        whenever(couponService.findRequestForIssue(couponId)).thenReturn(userId)

        // when
        couponScheduler.issueCoupons()

        verify(couponService, times(20)).issue(
            CouponCommand.Issue(
                userId = userId,
                couponId = couponId
            )
        )
    }

    private fun getTestCursor(rawKey: ByteArray): Cursor<ByteArray> {
        return object : Cursor<ByteArray> {
            private val it = listOf(rawKey).iterator()

            override fun hasNext(): Boolean = it.hasNext()
            override fun next(): ByteArray = it.next()
            override fun remove() = throw UnsupportedOperationException()
            override fun close() = Unit
            override fun isClosed(): Boolean = false

            override fun getPosition(): Long = 0L

            override fun getId(): Cursor.CursorId = object : Cursor.CursorId() {
                override fun getCursorId(): String = ""
            }

            override fun getCursorId(): Long = 0L
        }
    }
}
