package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductDetailView
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : ProductRepository {
    override fun findBy(id: Long): Product? {
        TODO("Not yet implemented")
    }

    override fun findAllDetailsBy(productId: Long): List<ProductDetailView>? {
        TODO("Not yet implemented")
    }
}