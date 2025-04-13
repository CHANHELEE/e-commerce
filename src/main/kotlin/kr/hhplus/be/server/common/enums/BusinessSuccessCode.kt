package kr.hhplus.be.server.common.enums

import kr.hhplus.be.server.presentation.common.BusinessCode

enum class BusinessSuccessCode(
    override val code: String,
    override val message: String,
    override val status: Int,
) : BusinessCode {

    //공통
    SUCCESS("COMMON-200", "요청이 성공적으로 처리되었습니다.", 200),
}