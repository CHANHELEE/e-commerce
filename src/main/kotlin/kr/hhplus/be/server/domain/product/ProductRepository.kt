package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.Product

interface ProductRepository {

    fun findById(id: Long): Product?
}