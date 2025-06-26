package io.mohammedalaamorsi.nyt.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val abstract: String,
    @SerialName("adx_keywords")
    val adxKeywords: String,
    @SerialName("asset_id")
    val assetId: Long,
    val byline: String,
    val column: String?=null,
    @SerialName("des_facet")
    val desFacet: List<String>,
    @SerialName("eta_id")
    val etaId: Int,
    @SerialName("geo_facet")
    val geoFacet: List<String>,
    val id: Long,
    val media: List<Media>,
    val nytdsection: String,
    @SerialName("org_facet")
    val orgFacet: List<String>,
    @SerialName("per_facet")
    val perFacet: List<String>,
    @SerialName("published_date")
    val publishedDate: String,
    val section: String,
    val source: String,
    val subsection: String,
    val title: String,
    val type: String,
    val updated: String,
    val uri: String,
    val url: String
)
