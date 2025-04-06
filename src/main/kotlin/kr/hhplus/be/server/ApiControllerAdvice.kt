package kr.hhplus.be.server

import kr.hhplus.be.server.presentation.common.ApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.common.enums.BusinessErrorCode
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(
    basePackages = ["kr.hhplus.be.server.presentation"]
)
class ApiResponseAdvice : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {

        return returnType.parameterType != ApiResponse::class.java
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any {

        if (body is ApiResponse<*>) {
            return body
        }

        val annotation = returnType.getMethodAnnotation(SuccessResponse::class.java)
        return if (annotation != null) {
            val code = annotation.value
            ApiResponse.success(
                data = body,
                code = code.code,
                message = code.message,
                status = code.status,
            )
        } else {
            ApiResponse.success(data = body)
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ApiResponse<Nothing>> {

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.error(
                    status = BusinessErrorCode.INTERNAL_SERVER_ERROR.status,
                    code = BusinessErrorCode.INTERNAL_SERVER_ERROR.code,
                    message = BusinessErrorCode.INTERNAL_SERVER_ERROR.message,
                )
            )
    }
}