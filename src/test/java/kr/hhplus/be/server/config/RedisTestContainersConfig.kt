package kr.hhplus.be.server.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@TestConfiguration
@Testcontainers
class RedisTestContainersConfig {

    companion object {
        private const val REDIS_PORT = 36379

        @JvmStatic
        val redisContainer = GenericContainer<Nothing>("redis").apply {
            withExposedPorts(REDIS_PORT)
            waitingFor(Wait.forListeningPort())
            withStartupTimeout(Duration.ofSeconds(60))
            start()
        }
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(
            redisContainer.host,
            redisContainer.getMappedPort(REDIS_PORT)
        )
    }
}