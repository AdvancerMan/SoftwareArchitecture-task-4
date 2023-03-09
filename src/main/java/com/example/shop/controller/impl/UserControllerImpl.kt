package com.example.shop.controller.impl

import com.example.shop.controller.ControllerHelper
import com.example.shop.controller.UserController
import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.server.ShopServer
import com.example.shop.service.ProductService
import com.example.shop.service.UserService
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import org.springframework.stereotype.Component
import rx.Observable
import java.io.ByteArrayOutputStream
import javax.annotation.PostConstruct

@Component
class UserControllerImpl(

    private val userService: UserService,

    private val shopServer: ShopServer,

    private val controllerHelper: ControllerHelper,
) : UserController {

    companion object {
        private val JSON_MAPPER = JsonMapper()
            .registerModule(KotlinModule.Builder().build())
    }

    @PostConstruct
    fun init() {
        shopServer.registerApi("/user/add/", HttpMethod.POST, ::addUser)
        shopServer.registerApi("/user/", HttpMethod.GET, ::getUser)
    }

    override fun addUser(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
        return controllerHelper.readBody(request, User::class.java)
            .flatMap { userService.addUser(it) }
            .flatMap { response.setStatus(HttpResponseStatus.CREATED) }
    }

    override fun getUser(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
        return request
            .queryParameters["userId"]
            ?.firstOrNull()
            ?.let { userService.getUser(it) }
            ?.switchIfEmpty(Observable.error(IllegalArgumentException()))
            ?.take(1)
            ?.map { JSON_MAPPER.writeValueAsString(it) }
            ?.flatMap { response.writeString(Observable.just(it)) }
            ?.onErrorResumeNext { response.setStatus(HttpResponseStatus.NOT_FOUND) }
            ?: response.setStatus(HttpResponseStatus.NOT_FOUND)
    }
}
