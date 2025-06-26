package io.mohammedalaamorsi.nyt.data.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
@Parcelize
@Serializable
data class MediaMetadata(
    val format: String,
    val height: Int,
    val url: String,
    val width: Int
): Parcelable
