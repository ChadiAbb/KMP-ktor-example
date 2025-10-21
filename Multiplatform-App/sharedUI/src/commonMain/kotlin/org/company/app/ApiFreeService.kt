package com.example.free_univ_app.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

@Serializable
data class Room(
    val name: String,
    val freeTime: Int
)

suspend fun fetchRooms(): List<Room> {
    return try {
        val response = httpClient.get("http://148.253.122.47:8080/free")
        println("Raw response: ${response.bodyAsText()}")
        if (response.bodyAsText().trim() == "[]") {
            emptyList()
        } else {
            response.body<List<Room>>()
        }
    } catch (e: Exception) {
        println("Error fetching rooms: ${e.message}")
        emptyList()
    }
}

private val roomsName = listOf("Halle", "Germain", "Gouges")


private fun getSimpleName(name : String) : String {
    var regex = Regex(""".*""")
    if (name.contains(roomsName[0])) {
        regex = Regex("""^.*?([\d]+[A-Z]).*$""")
    } else if (name.contains(roomsName[1])) {
        regex = Regex("""^.*?([\d]{4}).*$""")
    } else if (name.contains(roomsName[2])){
        regex = Regex("""^([^ _]+)""")
    }
    val matchResult = regex.find(name)
    return matchResult?.groupValues?.get(1) ?: name
}
