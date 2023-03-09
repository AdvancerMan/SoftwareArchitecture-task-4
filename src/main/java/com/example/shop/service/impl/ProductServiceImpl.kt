package com.example.shop.service.impl

import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.model.UserViewedProduct
import com.example.shop.repository.ProductRepository
import com.example.shop.repository.UserRepository
import com.example.shop.service.CurrencyConverterService
import com.example.shop.service.ProductService
import com.mongodb.rx.client.Success
import org.springframework.stereotype.Service
import rx.Observable

@Service
class ProductServiceImpl(

    private val productRepository: ProductRepository,

    private val userRepository: UserRepository,

    private val currencyConverterService: CurrencyConverterService,
) : ProductService {

    override fun addProduct(product: Product): Observable<Success> {
        return productRepository.addProduct(product)
    }

    override fun getAllProducts(forUserId: String): Observable<UserViewedProduct> {
        return userRepository
            .getUser(forUserId)
            .take(1)
            .flatMap { user ->
                productRepository
                    .getAllProducts()
                    .map { convertToUserViewedProduct(user, it) }
            }
    }

    private fun convertToUserViewedProduct(user: User, product: Product): UserViewedProduct {
        return UserViewedProduct(
            product.id,
            product.name,
            product.quantity,
            currencyConverterService.convertRublesTo(product.priceRubles, user.currencyType),
        )
    }
}
