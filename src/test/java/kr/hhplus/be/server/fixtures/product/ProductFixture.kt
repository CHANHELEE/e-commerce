package kr.hhplus.be.server.fixtures.product

import kr.hhplus.be.server.domain.product.model.ProductDetailView
import kr.hhplus.be.server.domain.product.model.entity.Product
import java.time.LocalDateTime

object ProductFixture {

    fun get(
        id: Long = 1L,
        name: String = "테스트 상품",
        price: Long = 10_000L,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now(),
    ): Product = Product(
        id = id,
        name = name,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}


object ProductDetailViewFixture {

    fun get(
        productId: Long = 1L,
        name: String = "테스트 상품",
        price: Long = 10_000L,
        size: String = "대",
        stock: Long = 100L,
    ): ProductDetailView = ProductDetailView(
        productId = productId,
        name = name,
        price = price,
        size = size,
        stock = stock,
    )
}