package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.common.model.PagingResult
import kr.hhplus.be.server.domain.product.model.ProductDetailView
import kr.hhplus.be.server.domain.product.model.entity.Product
import kr.hhplus.be.server.domain.product.model.entity.ProductStock
import org.springframework.data.domain.Pageable

interface ProductRepository {

    fun findBy(id: Long): Product?

    fun findAllDetailsBy(productId: Long): List<ProductDetailView>?

    fun findAllBy(pageable: Pageable): PagingResult<Product>?

    fun findStockBy(productId: Long, optionId: Long): ProductStock?

    fun findStockWithLockBy(productId: Long, optionId: Long): ProductStock?

    fun updateStock(productStock: ProductStock): ProductStock
}