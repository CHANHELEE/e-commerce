package kr.hhplus.be.server.presentation.common.enums

import kr.hhplus.be.server.presentation.common.BusinessCode

enum class BusinessErrorCode(
    override val code: String,
    override val message: String,
    override val status: Int,
) : BusinessCode {

    //공통
    INTERNAL_SERVER_ERROR("COMMON-500", "서버 오류가 발생했습니다.", 500),
    VALIDATION_ERROR("COMMON-400", "요청 항목 검증에 실패하였습니다.", 400),
}