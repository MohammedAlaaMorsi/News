package io.mohammedalaamorsi.nyt.data.remote

import io.ktor.client.HttpClient

interface HttpClientProvider {
    val httpClientImp: HttpClient
}
