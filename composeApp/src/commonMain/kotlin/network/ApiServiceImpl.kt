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


class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getThingSpeakValues(results: String): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get{
                url(HttpRoutes.REQUEST_URL)
                parameter("api_key", "ZL0IH5O2QK5U4NNS")
                parameter("results", results)
            }
        }

}


private suspend inline fun <reified T> makeRequest(crossinline request: suspend () -> HttpResponse): ResultNetwork<T> {
    return try {
        val response: HttpResponse = request()
        if (response.status == HttpStatusCode.OK) {
            ResultNetwork.success(response.body())
        } else {
            ResultNetwork.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ResultNetwork.failure(e)
    } catch (e: RedirectResponseException) {
        // 3xx - response
        println("Error: ${e.response.status.description}")
        ResultNetwork.failure(e)
        //ThingSpeakResponse(null, emptyList())
    } catch (e: ClientRequestException) {
        // 4xx - response
        println("Error: ${e.response.status.description}")
        ResultNetwork.failure(e)
        //ThingSpeakResponse(null, emptyList())
    } catch (e: ServerResponseException) {
        // 5xx - response
        println("Error: ${e.response.status.description}")
        ResultNetwork.failure(e)
        //ThingSpeakResponse(null, emptyList())
    } catch (e: Exception) {
        println("Error: ${e.message}")
        ResultNetwork.failure(e)
        //ThingSpeakResponse(null, emptyList())
    }
}
