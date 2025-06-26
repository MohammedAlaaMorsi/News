package io.mohammedalaamorsi.nyt.presentation.states.effects

sealed interface Effect {
    data class ShowSnackbarResource(val messageRes: String) : Effect
}
