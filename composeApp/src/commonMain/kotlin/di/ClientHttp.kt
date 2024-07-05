package di

import io.ktor.client.HttpClient

expect class ClientHttp {
    fun build(): HttpClient
}