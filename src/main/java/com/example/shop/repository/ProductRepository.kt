package com.example.shop.repository

import com.example.shop.model.Product
import com.mongodb.rx.client.Success
import rx.Observable

interface ProductRepository {

    fun addProduct(product: Product): Observable<Success>

    fun getAllProducts(): Observable<Product>
}