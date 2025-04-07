package kr.hhplus.be.server.common.enums

import kr.hhplus.be.server.presentation.common.BusinessCode

enum class BusinessErrorCode(
    override val code: String,
    override val message: String,
    override val status: Int,
) : BusinessCode {

    //공통
    INTERNAL_SERVER_ERROR("COMMON-500", "서버 오류가 발생했습니다.", 500),
    VALIDATION_ERROR("COMMON-400", "요청 항목 검증에 실패하였습니다.", 400),
    METHOD_ARGUMENT_TYPE_MISMATCH_ERROR("COMMON-401", "잘못된 형식의 값이 전달되었습니다.", 400),

    //상품
    PRODUCT_NOT_FOUND("PRODUCT-404", "존재하지 않는 상품 입니다.", 404),
}