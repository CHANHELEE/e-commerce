package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductOption
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : ProductRepository {
    override fun findById(id: Long): Product? {
        TODO("Not yet implemented")
    }

    override fun findAllOptionsBy(productId: Long): List<ProductOption>? {
        TODO("Not yet implemented")
    }
}