package kr.hhplus.be.server.infrastructure.api.payment

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class OrderClientConfig {

    @Bean
    fun orderDataPlatformRestClient(): RestClient {
        return RestClient.builder().baseUrl("https://mock.com").build()
    }

}
