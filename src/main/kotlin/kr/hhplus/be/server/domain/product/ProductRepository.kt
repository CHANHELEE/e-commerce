package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.ProductDetailView
import kr.hhplus.be.server.domain.product.model.entity.Product
import kr.hhplus.be.server.domain.product.model.entity.ProductStock

interface ProductRepository {

    fun findBy(id: Long): Product?

    fun findAllDetailsBy(productId: Long): List<ProductDetailView>?

    fun findStockBy(productId: Long, optionId: Long): ProductStock?

    fun findStockWithLockBy(productId: Long, optionId: Long): ProductStock?

    fun saveStock(productStock: ProductStock): ProductStock
}