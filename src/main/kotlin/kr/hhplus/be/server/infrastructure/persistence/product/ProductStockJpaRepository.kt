package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductStockEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductStockJpaRepository : JpaRepository<ProductStockEntity, Long> {

    fun findByProductIdAndProductOptionId(productId: Long, optionId: Long): ProductStockEntity?

    fun findWithLockByProductIdAndProductOptionId(productId: Long, optionId: Long): ProductStockEntity?
}