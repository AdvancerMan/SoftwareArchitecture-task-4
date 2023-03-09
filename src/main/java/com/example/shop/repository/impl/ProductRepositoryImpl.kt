package com.example.shop.repository.impl

import com.example.shop.model.Product
import com.example.shop.repository.ProductRepository
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import org.bson.Document
import org.springframework.stereotype.Repository
import rx.Observable

@Repository
class ProductRepositoryImpl(

    private val mongoDatabase: MongoDatabase,
) : ProductRepository {

    companion object {
        private const val COLLECTION_NAME = "product"
    }

    override fun addProduct(product: Product): Observable<Success> {
        val document = Document()
            .append("id", product.id)
            .append("name", product.name)
            .append("quantity", product.quantity)
            .append("priceRubles", product.priceRubles)

        return mongoDatabase
            .getCollection(COLLECTION_NAME)
            .insertOne(document)
    }

    override fun getAllProducts(): Observable<Product> {
        return mongoDatabase
            .getCollection(COLLECTION_NAME)
            .find()
            .toObservable()
            .map { convertToModel(it) }
    }

    private fun convertToModel(document: Document): Product {
        return Product(
            document.getString("id"),
            document.getString("name"),
            document.getLong("quantity"),
            document.getDouble("priceRubles"),
        )
    }
}
