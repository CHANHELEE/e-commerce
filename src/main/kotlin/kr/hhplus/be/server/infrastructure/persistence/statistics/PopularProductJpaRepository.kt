package kr.hhplus.be.server.infrastructure.persistence.statistics

import kr.hhplus.be.server.infrastructure.persistence.statistics.model.PopularProductProjection
import kr.hhplus.be.server.infrastructure.persistence.statistics.model.entity.PopularProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PopularProductJpaRepository : JpaRepository<PopularProductEntity, Long> {

    @Query(
        """
    SELECT 
    ranked.product_id AS productId,
    p.name AS name
    FROM (
        SELECT 
            op.product_id,
            SUM(op.amount) AS total_amount
        FROM orders_products op
        WHERE op.created_at > :startDate
        GROUP BY op.product_id
        ORDER BY total_amount DESC
        LIMIT 5
    ) ranked
    JOIN products p ON p.id = ranked.product_id
    ORDER BY ranked.total_amount DESC
    """,
        nativeQuery = true
    )
    fun findTop5BestSellingProductsSince(@Param("startDate") startDate: LocalDateTime): List<PopularProductProjection>
}