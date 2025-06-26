package io.mohammedalaamorsi.nyt.domain

import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getPopularNews(daysPeriod: Int): Flow<MostPopularApiResponse>
}
