package org.company.app.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

actual fun fetchRoomCall(callback: (List<Room>) -> Unit) {
    val httpClient = HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
        }
    }

    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        try {
            val response = httpClient.get("http://148.253.122.47:8080/free")
            println("Raw response: ${response.bodyAsText()}")
            val rooms = if (response.bodyAsText().trim() == "[]") {
                emptyList<Room>()
            } else {
                response.body<List<Room>>()
            }
            callback(rooms)
        } catch (e: Exception) {
            println("Error fetching rooms: ${e.message}")
            callback(emptyList())
        } finally {
            httpClient.close()
        }
    }
}