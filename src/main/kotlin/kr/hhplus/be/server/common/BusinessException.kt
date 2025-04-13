package kr.hhplus.be.server.common

import kr.hhplus.be.server.common.enums.BusinessErrorCode

class BusinessException(
    val errorCode: BusinessErrorCode
) : RuntimeException(errorCode.message)