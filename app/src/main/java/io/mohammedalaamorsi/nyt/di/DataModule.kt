package io.mohammedalaamorsi.nyt.di


import io.mohammedalaamorsi.nyt.data.remote.AndroidClientProvider
import io.mohammedalaamorsi.nyt.data.remote.HttpClientProvider
import io.mohammedalaamorsi.nyt.data.remote.KtorHttpClientService
import io.mohammedalaamorsi.nyt.data.remote.NewsUrlProvider
import io.mohammedalaamorsi.nyt.data.remote.RemoteNewsDataSource
import io.mohammedalaamorsi.nyt.data.remote.UrlsProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val dataModule = module {
    singleOf(::RemoteNewsDataSource)
    factory<HttpClientProvider> { AndroidClientProvider() }
    singleOf(::KtorHttpClientService)
    single<UrlsProvider> { NewsUrlProvider() }
}
