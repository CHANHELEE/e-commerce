package kr.hhplus.be.server.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@Testcontainers
class RedisTestContainersConfig {

    companion object {
        private const val REDIS_PORT = 6379

        @JvmStatic
        val redisContainer = GenericContainer<Nothing>("redis:7.2").apply {
            withExposedPorts(REDIS_PORT)
            waitingFor(Wait.forListeningPort())
            withStartupTimeout(Duration.ofSeconds(30))
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

    @Bean
    @Primary
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://${redisContainer.host}:${redisContainer.getMappedPort(REDIS_PORT)}")
        return Redisson.create(config)
    }
}