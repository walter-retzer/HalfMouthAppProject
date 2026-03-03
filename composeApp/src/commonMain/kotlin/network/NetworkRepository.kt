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
import secrets.BuildConfig


class NetworkRepository(private val client: HttpClient) {

    suspend fun updateFieldValue(
        fieldNumber1: Int = 1,
        fieldNumber2: Int = 2,
        fieldNumber3: Int = 3,
        fieldNumber4: Int = 4,
        fieldNumber5: Int = 5,
        fieldNumber6: Int = 6,
        fieldNumber7: Int = 7,
        fieldNumber8: Int = 8,
        value: Int = 10
    ): ResultNetwork<Int> =
        makeRequest {
            client.get{
                url(HttpRoutes.REQUEST_URL_UPDATE_FIELDS)
                parameter("api_key", BuildConfig.API_KEY_WRITE_TEMPERATURE)
                parameter("field$fieldNumber1", value.toString())
                parameter("field$fieldNumber2", value.toString())
                parameter("field$fieldNumber3", value.toString())
                parameter("field$fieldNumber4", value.toString())
                parameter("field$fieldNumber5", value.toString())
                parameter("field$fieldNumber6", value.toString())
                parameter("field$fieldNumber7", value.toString())
                parameter("field$fieldNumber8", value.toString())
            }
        }


    suspend fun getThingSpeakSetPointValues(): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get{
                url(HttpRoutes.REQUEST_URL_READ_SETPOINT)
                parameter("api_key", BuildConfig.API_KEY_READ_SETPOINT)
                parameter("results", BuildConfig.RESULTS)
            }
        }

    suspend fun getThingSpeakTemperatureValues(): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get{
                url(HttpRoutes.REQUEST_URL_READ_TEMPERATURE)
                parameter("api_key", BuildConfig.API_KEY_READ_TEMPERATURE)
                parameter("results", BuildConfig.RESULTS)
            }
        }

    suspend fun getThingSpeakDigitalInputValues(): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get{
                url(HttpRoutes.REQUEST_URL_READ_STATUS)
                parameter("api_key", BuildConfig.API_KEY_READ_STATUS)
                parameter("results", BuildConfig.RESULTS)
            }
        }

    suspend fun getThingSpeakChannelFeed(fieldId: String, results: String): ResultNetwork<ThingSpeakResponse> =
        makeRequest {
            client.get{
                url(HttpRoutes.REQUEST_CHANNEL_FEED + "$fieldId.json?")
                parameter("api_key", BuildConfig.API_KEY_READ_SETPOINT)
                parameter("results", results)
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
}
