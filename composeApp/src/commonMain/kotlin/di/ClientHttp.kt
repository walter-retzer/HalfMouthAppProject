package di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect class ClientHttp {
    fun build(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient
}