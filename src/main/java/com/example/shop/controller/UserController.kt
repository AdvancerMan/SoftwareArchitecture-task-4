package com.example.shop.controller

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

interface UserController {

    fun addUser(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void>

    fun getUser(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void>
}
