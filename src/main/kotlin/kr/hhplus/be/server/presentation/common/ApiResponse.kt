package kr.hhplus.be.server.presentation.common

import kr.hhplus.be.server.common.enums.BusinessSuccessCode

data class ApiResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {

        //@SuccessAnnotation 적용 시 적용
        fun <T> success(
            status: Int = 200,
            code: String,
            data: T?,
            message: String = "요청이 성공적으로 처리되었습니다.",
        ): ApiResponse<T> =
            ApiResponse(status = status, code = code, message = message, data = data)

        //@SuccessAnnotation 미적용 시 적용
        fun <T> success(
            data: T?,
        ): ApiResponse<T> =
            ApiResponse(
                status = BusinessSuccessCode.SUCCESS.status,
                code = BusinessSuccessCode.SUCCESS.code,
                message = BusinessSuccessCode.SUCCESS.message,
                data = data
            )

        fun <T> error(
            status: Int = 400,
            code: String,
            message: String = "잘못된 요청입니다.",
        ): ApiResponse<T> =
            ApiResponse(status = status, code = code, message = message, data = null)
    }
}