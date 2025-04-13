package kr.hhplus.be.server.domain.common.model

data class PagingResult<T>(
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Long,
    val data: List<T>
)
