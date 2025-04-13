package kr.hhplus.be.server.presentation.common.annotation

import kr.hhplus.be.server.common.enums.BusinessSuccessCode

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SuccessResponse(
    val value: BusinessSuccessCode = BusinessSuccessCode.SUCCESS
)