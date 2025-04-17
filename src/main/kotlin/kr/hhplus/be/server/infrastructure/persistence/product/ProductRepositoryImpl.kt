package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.ProductDetailView
import kr.hhplus.be.server.domain.product.model.entity.Product
import kr.hhplus.be.server.domain.product.model.entity.ProductStock
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductStockEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
    private val productStockJpaRepository: ProductStockJpaRepository,
) : ProductRepository {
    override fun findBy(productId: Long): Product? {
        return productJpaRepository.findByIdOrNull(productId)?.toDomain()
    }

    override fun findAllDetailsBy(productId: Long): List<ProductDetailView>? {
        val productDetailProjections = productJpaRepository.findAllDetailsBy(productId)
        val views = productDetailProjections.map {
            ProductDetailView(
                productId = it.productId,
                name = it.name,
                price = it.price,
                size = it.size,
                stock = it.stock
            )
        }
        return views
    }

    override fun findStockBy(productId: Long, optionId: Long): ProductStock? {
        return productStockJpaRepository.findByProductIdAndProductOptionId(productId, optionId)?.toDomain()
    }

    override fun findStockWithLockBy(productId: Long, optionId: Long): ProductStock? {
        return productStockJpaRepository.findWithLockByProductIdAndProductOptionId(productId, optionId)?.toDomain()
    }

    override fun saveStock(productStock: ProductStock): ProductStock {
        return productStockJpaRepository.save(ProductStockEntity.from(productStock)).toDomain()
    }
}