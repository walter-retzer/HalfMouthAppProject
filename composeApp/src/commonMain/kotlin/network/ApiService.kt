package network

import data.ThingSpeakResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


interface ApiService {

    suspend fun getThingSpeakValues(results: String): ResultNetwork<ThingSpeakResponse>

    suspend fun getThingSpeakChannelFeed(fieldId: String, results: String): ResultNetwork<ThingSpeakResponse>

//    companion object {
//        fun create(): ApiService
//    }
}