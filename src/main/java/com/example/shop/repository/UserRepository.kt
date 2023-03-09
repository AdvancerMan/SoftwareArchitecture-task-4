package com.example.shop.repository

import com.example.shop.model.User
import com.mongodb.rx.client.Success
import rx.Observable

interface UserRepository {

    fun addUser(user: User): Observable<Success>

    fun getUser(userId: String): Observable<User>
}
