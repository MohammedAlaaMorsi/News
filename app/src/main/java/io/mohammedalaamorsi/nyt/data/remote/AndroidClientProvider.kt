package io.mohammedalaamorsi.nyt.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AndroidClientProvider : HttpClientProvider {
    override val httpClientImp: HttpClient
        get() = HttpClient(Android) {
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.HEADERS
            }
            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                }
                json(json)
            }
        }
}
