package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductOptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductOptionJpaRepository : JpaRepository<ProductOptionEntity, Long>