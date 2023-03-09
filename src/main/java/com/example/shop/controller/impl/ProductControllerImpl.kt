package com.example.shop.controller.impl

import com.example.shop.controller.ControllerHelper
import com.example.shop.controller.ProductController
import com.example.shop.model.Product
import com.example.shop.server.ShopServer
import com.example.shop.service.ProductService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.node.ArrayNode
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
class ProductControllerImpl(

    private val productService: ProductService,

    private val shopServer: ShopServer,

    private val controllerHelper: ControllerHelper,
) : ProductController {

    companion object {
        private val JSON_MAPPER = JsonMapper()
            .registerModule(KotlinModule.Builder().build())
    }

    @PostConstruct
    fun init() {
        shopServer.registerApi("/product/add/", HttpMethod.POST, ::addProduct)
        shopServer.registerApi("/product/all/", HttpMethod.GET, ::getAllProducts)
    }

    override fun addProduct(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
        return controllerHelper.readBody(request, Product::class.java)
            .flatMap { productService.addProduct(it) }
            .flatMap { response.setStatus(HttpResponseStatus.CREATED) }
    }

    override fun getAllProducts(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
        return request
            .queryParameters["forUserId"]
            ?.firstOrNull()
            ?.let { productService.getAllProducts(it) }
            ?.reduce(JSON_MAPPER.createArrayNode()) { json, product ->
                json.add(JSON_MAPPER.valueToTree<JsonNode>(product))
                json
            }
            ?.map { JSON_MAPPER.writeValueAsString(it) }
            ?.let { response.writeString(it) }
            ?: response.setStatus(HttpResponseStatus.NOT_FOUND)
    }
}
