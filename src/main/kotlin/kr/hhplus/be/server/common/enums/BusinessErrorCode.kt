package kr.hhplus.be.server.common.enums

import kr.hhplus.be.server.domain.point.model.entity.Point
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
    PRODUCT_OPTIONS_NOT_FOUND("PRODUCT-405", "상품 옵션이 존재하지 않습니다.", 404),
    PRODUCTS_NOT_EXIST("PRODUCT-406", "조회 가능한 상품이 없습니다.", 404),
    PRODUCT_STOCK_OUT_OF_STOCK("PRODUCT-407", "주문 상품 재고가 부족합니다.", 404),

    //포인트
    INVALID_POINT_CHARGE_AMOUNT("POINT-400", "포인트 충전시 ${Point.MIN}원 이상의 값만 충전 가능 합니다. ", 400),
    EXCEED_POINT_LIMIT("POINT-400", "보유 포인트는 ${Point.MAX} 을 초과 할 수 없습니다.", 400),
    USER_POINT_NOT_FOUND("POINT-404", "해당 사용자 포인트를 조회할 수 없습니다.", 404),
    POINT_NOT_ENOUGH("POINT-405", "해당 사용자 포인트 잔액이 부족합니다.", 404),

    //쿠폰
    USER_COUPON_NOT_EXIST("USER-COUPON-404", "해당 사용자 쿠폰을 조회할 수 없습니다.", 404),
    USER_COUPON_ALREADY_USED("USER-COUPON-400", "해당 사용자 쿠폰은 이미 사용되었습니다.", 400),
    COUPON_ALREADY_ISSUED("USER-COUPON-409", "이미 발급된 쿠폰입니다.", 409),
    COUPON_NOT_EXIST("COUPON-404", "해당 쿠폰을 조회할 수 없습니다.", 404),
    COUPON_OUT_OF_AMOUNT("COUPON-405", "해당 쿠폰은 모두 소진되었습니다.", 409),

    //주문
    ORDER_NOT_EXIST("ORDER-404", "조회 가능한 주문이 없습니다.", 404),
    ORDER_PRODUCT_NOT_EXIST("ORDER-PRODUCT-404", "조회 가능한 주문 상품이 없습니다.", 404),
    ORDER_ALREADY_COMPLETED("ORDER-400", "이미 완료된 주문입니다.", 400),

    //통계
    POPULAR_PRODUCTS_NOT_EXIST("STATISTIC-PRODUCT-404", "조회 가능한 인기 상품이 없습니다.", 404),
}