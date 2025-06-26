package io.mohammedalaamorsi.nyt.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed interface Screens {
    @Parcelize
    @Serializable
    data object NewsList : Screens, Parcelable

    @Parcelize
    @Serializable
    data class NewsDetails(
        val id: String
    ) : Screens, Parcelable
}
