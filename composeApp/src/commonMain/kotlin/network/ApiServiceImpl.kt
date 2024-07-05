package network

import data.ThingSpeakResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class ApiServiceImpl {

    suspend fun getThingSpeakValues(results: String): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get {
                url(HttpRoutes.REQUEST_URL)
                parameter("api_key", "ZL0IH5O2QK5U4NNS")
                parameter("results", results)
            }
        }

    suspend fun getThingSpeakChannelFeed(
        fieldId: String,
        results: String
    ): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get {
                url(HttpRoutes.REQUEST_CHANNEL_FEED + "$fieldId.json?")
                parameter("api_key", "ZL0IH5O2QK5U4NNS")
                parameter("results", results)
            }
        }


    suspend inline fun <reified T> makeRequest(crossinline request: suspend () -> HttpResponse): ResultNetwork<T> {
        return try {
            val response: HttpResponse = request()
            if (response.status == HttpStatusCode.OK) {
                ResultNetwork.success(response.body())
            } else {
                ResultNetwork.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: RedirectResponseException) {
            // 3xx - response
            println("Error: ${e.response.status.description}")
            ResultNetwork.failure(e)
        } catch (e: ClientRequestException) {
            // 4xx - response
            println("Error: ${e.response.status.description}")
            ResultNetwork.failure(e)
        } catch (e: ServerResponseException) {
            // 5xx - response
            println("Error: ${e.response.status.description}")
            ResultNetwork.failure(e)
        } catch (e: Exception) {
            println("Error: ${e.printStackTrace()}")
            ResultNetwork.failure(e)
        }
    }

    companion object: KoinComponent{
        val client: HttpClient by inject()
    }
}