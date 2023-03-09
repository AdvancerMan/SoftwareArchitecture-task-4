package com.example.shop.service

import com.example.shop.model.User
import com.mongodb.rx.client.Success
import rx.Observable

interface UserService {

    fun addUser(user: User): Observable<Success>

    fun getUser(userId: String): Observable<User>
}
