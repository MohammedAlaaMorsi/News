package io.mohammedalaamorsi.nyt.presentation.states.events


sealed interface NewsEvent {
    data class FetchNews(val daysPeriod:Int) : NewsEvent
}
