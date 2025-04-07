package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductOption

interface ProductRepository {

    fun findById(id: Long): Product?

    fun findAllOptionsBy(productId: Long): List<ProductOption>?
}