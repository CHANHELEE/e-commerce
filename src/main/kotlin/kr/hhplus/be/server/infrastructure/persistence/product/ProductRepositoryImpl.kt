package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : ProductRepository {
    override fun findById(id: Long): Product? {
        TODO("Not yet implemented")
    }
}