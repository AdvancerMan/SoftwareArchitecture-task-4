package com.example.shop.server

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpMethod
import io.reactivex.netty.protocol.http.server.RequestHandler

interface ShopServer {

    fun registerApi(path: String, method: HttpMethod, handler: RequestHandler<ByteBuf, ByteBuf>)
}
