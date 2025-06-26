package io.mohammedalaamorsi.nyt.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostPopularApiResponse(
    val copyright: String,
    @SerialName("num_results")
    val numResults: Int,
    val results: List<Result>,
    val status: String,
)
