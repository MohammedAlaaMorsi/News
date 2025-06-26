package io.mohammedalaamorsi.nyt.data.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Media(
    @SerialName("approved_for_syndication")
    val approvedForSyndication: Int,
    val caption: String,
    val copyright: String,
    @SerialName("media-metadata")
    val mediaMetadata: List<MediaMetadata>,
    val subtype: String,
    val type: String
):Parcelable
