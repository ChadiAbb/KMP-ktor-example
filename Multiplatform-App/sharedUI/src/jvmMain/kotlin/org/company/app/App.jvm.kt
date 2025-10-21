package org.company.app

import com.example.free_univ_app.network.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

actual fun fetchRoomCall(callback: (List<Room>) -> Unit) {
    val httpClient = HttpClient(OkHttp) {
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

    val scope = CoroutineScope(Dispatchers.IO)
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