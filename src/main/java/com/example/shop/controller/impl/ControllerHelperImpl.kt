package com.example.shop.controller.impl

import com.example.shop.controller.ControllerHelper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import org.springframework.stereotype.Service
import rx.Observable
import java.io.ByteArrayOutputStream

@Service
class ControllerHelperImpl : ControllerHelper {

    companion object {
        private val JSON_MAPPER = JsonMapper()
            .registerModule(KotlinModule.Builder().build())
    }

    override fun <T> readBody(request: HttpServerRequest<ByteBuf>, clazz: Class<T>): Observable<T> {
        return request
            .content
            .reduce(ByteArrayOutputStream()) { byteArrayOutputStream, bytes ->
                val bytesArray = ByteArray(bytes.capacity()) { bytes.getByte(it) }
                @Suppress("BlockingMethodInNonBlockingContext") // write is actually non-blocking here
                byteArrayOutputStream.write(bytesArray)
                byteArrayOutputStream
            }
            .map { byteArrayOutputStream ->
                val bytes = byteArrayOutputStream.toByteArray()
                val requestSize = (0 until bytes.size - 1)
                    .firstOrNull { bytes[it] == bytes[it + 1] && bytes[it] == '\n'.code.toByte() }
                    ?: bytes.size
                bytes.copyOf(requestSize)
            }
            .map { JSON_MAPPER.readValue(it, clazz) }
    }
}