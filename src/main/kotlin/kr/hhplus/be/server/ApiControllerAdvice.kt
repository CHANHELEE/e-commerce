package kr.hhplus.be.server

import kr.hhplus.be.server.common.BusinessException
import kr.hhplus.be.server.presentation.common.ApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.common.enums.BusinessErrorCode
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errorMessage =
            e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: BusinessErrorCode.VALIDATION_ERROR.message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.error(
                BusinessErrorCode.VALIDATION_ERROR.status,
                BusinessErrorCode.VALIDATION_ERROR.code,
                errorMessage,
            )
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Nothing>> {
        val paramName = e.name
        val requiredType = e.requiredType?.simpleName ?: "요청 타입"
        val message = "$paramName 은(는) $requiredType 타입이어야 합니다."

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.error(
                status = BusinessErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH_ERROR.status,
                code = BusinessErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH_ERROR.code,
                message = message
            )
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ApiResponse<Nothing>> {
        val error = e.errorCode
        return ResponseEntity.status(error.status).body(
            ApiResponse.error(
                status = error.status,
                code = error.code,
                message = error.message
            )
        )
    }
}