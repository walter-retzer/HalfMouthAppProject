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

    suspend fun getThingSpeakValues(results: String): ResultType<ThingSpeakResponse>

    companion object {
        fun create(): ApiService {
            return ApiServiceImpl(
                client = HttpClient {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
                    }
                }
            )
        }
    }
}