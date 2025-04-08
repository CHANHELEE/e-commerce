package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductDetailView

interface ProductRepository {

    fun findBy(id: Long): Product?

    fun findAllDetailsBy(productId: Long): List<ProductDetailView>?
}