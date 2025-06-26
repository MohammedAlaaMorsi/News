package io.mohammedalaamorsi.nyt.data.remote

import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import kotlinx.coroutines.flow.Flow

class RemoteNewsDataSource(
    private val ktorHttpClientService: KtorHttpClientService,
    private val urlsProvider: UrlsProvider,
) {
    fun getPopularNews(daysPeriod: Int): Flow<MostPopularApiResponse> {
        return ktorHttpClientService.loadRemoteData(
            apiPath = urlsProvider.getPopularNews(daysPeriod = daysPeriod),
            serializer = MostPopularApiResponse.serializer(),
        )
    }
}
