package kr.hhplus.be.server.presentation.common.model

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PagingResponse<T>(
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Long,
    val data: List<T>
)


data class PagingRequest(
    @field:Min(1, message = "페이지 번호(page)는 1 이상이어야 합니다.")
    val page: Int = 1,
    @field:Min(1, message = "페이지 크기(size)는 1 이상이어야 합니다.")
    @field:Max(100, message = "페이지 크기(size)는 100 이하이어야 합니다.")
    val size: Int = 10,
)