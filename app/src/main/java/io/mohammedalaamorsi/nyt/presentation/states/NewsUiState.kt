package io.mohammedalaamorsi.nyt.presentation.states

import io.mohammedalaamorsi.nyt.util.UiText


sealed class NewsUiState {
    data object Loading : NewsUiState()
    data object Empty : NewsUiState()
    data class Result(val data: List<io.mohammedalaamorsi.nyt.data.models.Result>) : NewsUiState()
    data class Error(val errorMessage: UiText) : NewsUiState()
}
