package io.mohammedalaamorsi.nyt.navigation

import android.os.Parcelable
import io.mohammedalaamorsi.nyt.data.models.Result
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed interface Screens {
    @Parcelize
    @Serializable
    data object AdaptiveNews : Screens, Parcelable

    @Parcelize
    @Serializable
    data object NewsList : Screens, Parcelable

    @Parcelize
    @Serializable
    data class NewsDetails(
        val item: Result
    ) : Screens, Parcelable
}
