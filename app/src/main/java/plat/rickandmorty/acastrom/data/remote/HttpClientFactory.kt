package plat.rickandmorty.acastrom.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"
    private const val CONNECT_TIMEOUT_MILLIS = 5_000L
    private const val REQUEST_TIMEOUT_MILLIS = 10_000L

    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true

            defaultRequest {
                url(BASE_URL)
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.INFO
            }

            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
                requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
            }
        }
    }
}
