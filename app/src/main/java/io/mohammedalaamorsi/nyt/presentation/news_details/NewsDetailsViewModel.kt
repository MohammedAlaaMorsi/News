package io.mohammedalaamorsi.nyt.presentation.news_details

import androidx.lifecycle.ViewModel
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.presentation.states.NewsDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class NewsDetailsViewModel(
    item: Result,
) : ViewModel() {

    private val _state = MutableStateFlow(NewsDetailsUiState())
    val state: StateFlow<NewsDetailsUiState> = _state

    init {
        showItemDetails(item)
    }

    private fun showItemDetails(item: Result) {
        _state.update {
            it.copy(item = item)
        }
    }

}
