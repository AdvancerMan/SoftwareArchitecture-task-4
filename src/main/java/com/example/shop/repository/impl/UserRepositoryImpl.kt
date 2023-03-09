package com.example.shop.repository.impl

import com.example.shop.model.CurrencyType
import com.example.shop.model.User
import com.example.shop.repository.UserRepository
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import org.bson.Document
import org.springframework.stereotype.Repository
import rx.Observable

@Repository
class UserRepositoryImpl(

    private val mongoDatabase: MongoDatabase,
) : UserRepository {

    companion object {
        private const val COLLECTION_NAME = "user"
    }

    override fun addUser(user: User): Observable<Success> {
        val document = Document()
            .append("id", user.id)
            .append("currencyType", user.currencyType.name)

        return mongoDatabase
            .getCollection(COLLECTION_NAME)
            .insertOne(document)
    }

    override fun getUser(userId: String): Observable<User> {
        return mongoDatabase
            .getCollection(COLLECTION_NAME)
            .find()
            .toObservable()
            .filter { it.getString("id") == userId }
            .map { convertToModel(it) }
    }

    private fun convertToModel(document: Document): User {
        return User(
            document.getString("id"),
            CurrencyType.valueOf(document.getString("currencyType")),
        )
    }
}
