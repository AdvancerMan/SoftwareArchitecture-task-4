package com.example.shop.controller

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import rx.Observable

interface ControllerHelper {

    fun <T> readBody(request: HttpServerRequest<ByteBuf>, clazz: Class<T>): Observable<T>
}
