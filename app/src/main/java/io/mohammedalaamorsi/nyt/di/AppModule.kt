package io.mohammedalaamorsi.nyt.di

import android.app.Application
import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single { DispatchersProvider() }
    single { Application() }
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
            prettyPrint = true
            encodeDefaults = true
            useAlternativeNames = true
        }
    }
}
