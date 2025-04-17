package kr.hhplus.be.server.presentation.product

import io.swagger.v3.oas.annotations.Operation
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.ProductCommand
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import kr.hhplus.be.server.presentation.common.annotation.SuccessResponse
import kr.hhplus.be.server.presentation.product.model.ProductResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {


    @Operation(
        summary = "상품 조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "상품 조회"
            )
        ]
    )
    @GetMapping("{productId}")
    @SuccessResponse
    fun product(@PathVariable productId: Long): ProductResponse.Product {

        val product = productService.getBy(ProductCommand.Product(productId))
        return ProductResponse.Product(product.id, product.name, product.price, product.updatedAt)
    }

    @Operation(
        summary = "상품 옵션 및  조회",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "상품 옵션 조회"
            )
        ]
    )
    @GetMapping("{productId}/options")
    @SuccessResponse
    fun productOptions(@PathVariable productId: Long): List<ProductResponse.Detail> {

        val productDetails = productService.getDetailsBy(ProductCommand.Detail(productId))
        return productDetails.map {
            ProductResponse.Detail(
                it.productId,
                it.name,
                it.price,
                it.size,
                it.stock
            )
        }
    }
}
