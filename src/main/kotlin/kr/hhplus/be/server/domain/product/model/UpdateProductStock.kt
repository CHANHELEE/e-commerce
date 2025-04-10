package kr.hhplus.be.server.domain.product.model

data class UpdateProductStock(
    val stockId: Long,
    val stock: Long,
)
