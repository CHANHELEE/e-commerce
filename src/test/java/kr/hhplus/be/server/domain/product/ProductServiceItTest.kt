package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.ProductCommand
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductOptionEntity
import kr.hhplus.be.server.infrastructure.persistence.product.model.entity.ProductStockEntity
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class ProductServiceItTest : IntegrationTestSupport() {

    @Autowired
    lateinit var productService: ProductService


    @Test
    fun `상품을 조회에 성공한다`() {

        //given
        val product = productJpaRepository.save(
            ProductEntity(
                name = "test",
                price = 1000L,
            )
        )

        //when
        val view = productService.getBy(ProductCommand.Product(product.id))

        //then
        assertThat(view.name).isEqualTo(product.name)
        assertThat(view.price).isEqualTo(product.price)
    }

    @Test
    fun `상품 옵션 상세 정보 조회에 성공한다`() {

        //given
        val product = productJpaRepository.save(
            ProductEntity(
                name = "test",
                price = 1000L,
            )
        )

        val option = productOptionJpaRepository.save(
            ProductOptionEntity(
                productId = product.id,
                size = "대",
                stock = 10L,
            )
        )

        //when
        val view = productService.getDetailsBy(ProductCommand.Detail(product.id))

        //then
        assertThat(view)
            .extracting("productId", "name", "price", "size", "stock")
            .containsExactlyInAnyOrder(
                Assertions.tuple(
                    product.id,
                    product.name,
                    product.price,
                    option.size,
                    option.stock,
                )
            )
    }

    @Nested
    inner class DecreaseStock {
        @Test
        fun `재고를 감소에 성공한다`() {

            //given
            val decreaseStock = 5
            val product = productJpaRepository.save(
                ProductEntity(
                    name = "test",
                    price = 1000L,
                )
            )

            val option = productOptionJpaRepository.save(
                ProductOptionEntity(
                    productId = product.id,
                    size = "대",
                    stock = 10L,
                )
            )

            val stock = productStockJpaRepository.save(
                ProductStockEntity(
                    productId = product.id,
                    productOptionId = option.id,
                    stock = 10L,
                )
            )

            //when
            val result = productService.decreaseStock(
                ProductCommand.UpdateStock(
                    productId = product.id,
                    optionId = option.id,
                    amount = 5L
                )
            )

            //then
            assertThat(result.stock).isEqualTo(stock.stock - decreaseStock)
        }
    }

    @Test
    fun `동시성 테스트 - 동시에 재고 차감 시 정확히 처리되어야 한다`() {
        // given
        val product = productJpaRepository.save(
            ProductEntity(
                name = "동시성상품",
                price = 10000L
            )
        )

        val option = productOptionJpaRepository.save(
            ProductOptionEntity(
                productId = product.id,
                size = "L",
                stock = 100L
            )
        )

        val stock = productStockJpaRepository.save(
            ProductStockEntity(
                productId = product.id,
                productOptionId = option.id,
                stock = 100L // 초기 재고
            )
        )

        val decreasePerThread = 2L
        val threadCount = 30
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        // when
        repeat(threadCount) {
            executorService.submit {
                try {
                    productService.decreaseStock(
                        ProductCommand.UpdateStock(
                            productId = product.id,
                            optionId = option.id,
                            amount = decreasePerThread
                        )
                    )
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        val result = productStockJpaRepository.findByProductIdAndProductOptionId(product.id, option.id)!!
        val totalDecreased = decreasePerThread * threadCount

        assertThat(result.stock).isEqualTo(stock.stock - totalDecreased)
        assertThat(result.stock).isGreaterThanOrEqualTo(0)
    }
}