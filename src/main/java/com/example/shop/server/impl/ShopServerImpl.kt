package com.example.shop.server.impl

import com.example.shop.server.ShopServer
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.RequestHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.annotation.PostConstruct

@Component
class ShopServerImpl(

    @Value("\${rx.server.port}")
    private val serverPort: Int,
) : ShopServer {

    private val apiHandlers: ConcurrentMap<Pair<String, HttpMethod>, RequestHandler<ByteBuf, ByteBuf>> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        HttpServer
            .newServer(serverPort)
            .start { request, response ->
                val handler = apiHandlers[request.decodedPath to request.httpMethod]
                    ?: return@start response.setStatus(HttpResponseStatus.NOT_FOUND)
                response.setHeader("Content-Type", "application/json")

                return@start handler.handle(request, response)
                    .onErrorResumeNext {
                        response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR)
                    }
            }
    }

    override fun registerApi(path: String, method: HttpMethod, handler: RequestHandler<ByteBuf, ByteBuf>) {
        apiHandlers[path to method] = handler
    }
}
