package kr.hhplus.be.server.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("커머스 프로젝트 API")
                    .description("1차 배포용 Happy case mock-api 입니다")
                    .version("1.0.0")
            )
    }
}