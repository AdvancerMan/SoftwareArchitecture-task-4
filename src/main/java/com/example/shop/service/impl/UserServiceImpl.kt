package com.example.shop.service.impl

import com.example.shop.model.User
import com.example.shop.repository.UserRepository
import com.example.shop.service.UserService
import com.mongodb.rx.client.Success
import org.springframework.stereotype.Service
import rx.Observable

@Service
class UserServiceImpl(

    private val userRepository: UserRepository,
) : UserService {

    override fun addUser(user: User): Observable<Success> {
        return userRepository.addUser(user)
    }

    override fun getUser(userId: String): Observable<User> {
        return userRepository.getUser(userId)
    }
}
