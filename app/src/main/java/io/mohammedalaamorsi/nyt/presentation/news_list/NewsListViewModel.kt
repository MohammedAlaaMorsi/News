package io.mohammedalaamorsi.nyt.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mohammedalaamorsi.nyt.R
import io.mohammedalaamorsi.nyt.domain.usecase.GetPopularNewsUseCase
import io.mohammedalaamorsi.nyt.presentation.states.effects.Effect
import io.mohammedalaamorsi.nyt.presentation.states.NewsUiState
import io.mohammedalaamorsi.nyt.presentation.states.events.NewsEvent
import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import io.mohammedalaamorsi.nyt.util.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class NewsListViewModel(
    private val getPopularNewsUseCase: GetPopularNewsUseCase,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _effects = MutableSharedFlow<Effect>()
    val effects = _effects.asSharedFlow()

    private val _state = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val state: StateFlow<NewsUiState> = _state

    init {
        onEvent(NewsEvent.FetchNews(7)) // Fetch news for the last 7 days by default
    }

    fun onEvent(event: NewsEvent) = viewModelScope.launch(dispatchersProvider.io) {
        when (event) {
            is NewsEvent.FetchNews -> {
                doFetch(event.daysPeriod)
            }
        }
    }


    private suspend fun doFetch(daysPeriod: Int) {
        getPopularNewsUseCase.invoke(
            daysPeriod
        ).catch { error ->
            _state.value = NewsUiState.Error(
                UiText.StringResource(
                    R.string.error_fetching_news,
                    listOf(error.message ?: "Unknown error")
                )
            )

        }.collect { result ->
            if (result.results.isNotEmpty()) {
                _state.value = NewsUiState.Result(result.results)
            }
        }
    }


} 
