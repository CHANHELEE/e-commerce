package kr.hhplus.be.server.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestEventPublisherConfig {

    @Bean
    fun applicationEventPublisher(context: ApplicationContext): ApplicationEventPublisher {
        return context
    }
}