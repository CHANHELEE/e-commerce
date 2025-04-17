package kr.hhplus.be.server.infrastructure.persistence.product

import kr.hhplus.be.server.infrastructure.persistence.product.model.ProductDetailProjection
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductJpaRepository : JpaRepository<ProductEntity, Long> {

    @Query(
        """
    SELECT
        p.id AS productId,
        p.name AS name,
        p.price AS price,
        o.size AS size,
        o.stock AS stock
    FROM products p
    JOIN  products_options o ON o.product_id = p.id
    WHERE p.id = :productId
    """,
        nativeQuery = true
    )
    fun findAllDetailsBy(@Param("productId") productId: Long): List<ProductDetailProjection>

}