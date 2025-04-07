package kr.hhplus.be.server.fixtures.product

import kr.hhplus.be.server.domain.product.model.ProductCommand

object ProductCommandFixture {

    fun get(
        productId: Long = 1L,
    ): ProductCommand.Product = ProductCommand.Product(
        productId = productId,
    )
}

object ProductOptionCommandFixture {

    fun get(
        productId: Long = 1L,
    ): ProductCommand.ProductOption = ProductCommand.ProductOption(
        productId = productId,
    )
}