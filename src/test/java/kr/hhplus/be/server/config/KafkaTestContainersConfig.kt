package kr.hhplus.be.server.config

import org.springframework.context.annotation.Configuration
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@Testcontainers
class KafkaTestContainersConfig {
    companion object {
        private val kafkaContainer: KafkaContainer = KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0"),
        ).apply {
            portBindings = listOf("9092:9092")
            start()
        }

        val bootstrapServers: String
            get() = kafkaContainer.bootstrapServers
    }
}