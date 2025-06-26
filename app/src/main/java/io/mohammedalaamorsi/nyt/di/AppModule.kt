package io.mohammedalaamorsi.nyt.di

import android.app.Application
import io.mohammedalaamorsi.nyt.presentation.news_details.NewsDetailsViewModel
import io.mohammedalaamorsi.nyt.presentation.news_list.NewsListViewModel
import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        viewModelOf(::NewsListViewModel)
        viewModel { params ->
            NewsDetailsViewModel(
                item = params.get(),
            )
        }
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
