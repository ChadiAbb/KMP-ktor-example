package org.company.app

import com.example.free_univ_app.network.Room
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LoggingFormat
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

actual fun fetchRoomCall(callback: (List<Room>) -> Unit) {
    val httpClient = HttpClient (Js) {
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

    MainScope().launch {  // Use MainScope for web
        try {
            val response = httpClient.get("http://148.253.122.47:8080/free") {
                header(HttpHeaders.AccessControlAllowOrigin, "*")
            }
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