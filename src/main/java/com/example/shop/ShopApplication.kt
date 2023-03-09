package com.example.shop

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.Clock

@SpringBootApplication
class ShopApplication {

	@Bean
	fun clock(): Clock {
		return Clock.systemUTC()
	}

	@Bean
	fun mongoClient(

		@Value("\${mongo.db.url}")
		mongoUrl: String,
	): MongoClient {
		return MongoClients.create(mongoUrl)
	}

	@Bean
	fun mongoDatabase(

		mongoClient: MongoClient,

		@Value("\${mongo.db.name}")
		mongoDbName: String,
	): MongoDatabase {
		return mongoClient.getDatabase(mongoDbName)
	}
}

fun main(args: Array<String>) {
	runApplication<ShopApplication>(*args)
}
