package com.example.shop.service

import com.example.shop.model.Product
import com.example.shop.model.UserViewedProduct
import com.mongodb.rx.client.Success
import rx.Observable

interface ProductService {

    fun addProduct(product: Product): Observable<Success>

    fun getAllProducts(forUserId: String): Observable<UserViewedProduct>
}
