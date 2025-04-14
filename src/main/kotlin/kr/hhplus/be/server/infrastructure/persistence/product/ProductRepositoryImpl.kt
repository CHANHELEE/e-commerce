package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.domain.common.model.PagingResult
import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.ProductDetailView
import kr.hhplus.be.server.domain.product.model.entity.Product
import kr.hhplus.be.server.domain.product.model.entity.ProductStock
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : ProductRepository {
    override fun findBy(id: Long): Product? {
        TODO("Not yet implemented")
    }

    override fun findAllDetailsBy(productId: Long): List<ProductDetailView>? {
        TODO("Not yet implemented")
    }

    override fun findAllBy(pageable: Pageable): PagingResult<Product>? {
        TODO("Not yet implemented")
    }

    override fun findStockBy(productId: Long, optionId: Long): ProductStock? {
        TODO("Not yet implemented")
    }

    override fun findStockWithLockBy(productId: Long, optionId: Long): ProductStock? {
        TODO("Not yet implemented")
    }

    override fun updateStock(productStock: ProductStock): ProductStock {
        TODO("Not yet implemented")
    }
}