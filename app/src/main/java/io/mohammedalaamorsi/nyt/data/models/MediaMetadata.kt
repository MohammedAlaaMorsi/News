package io.mohammedalaamorsi.nyt.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaMetadata(
    val format: String,
    val height: Int,
    val url: String,
    val width: Int
)
