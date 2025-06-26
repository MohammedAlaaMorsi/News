package io.mohammedalaamorsi.nyt.data.repository


import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import io.mohammedalaamorsi.nyt.data.remote.RemoteNewsDataSource
import io.mohammedalaamorsi.nyt.domain.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImp(
    private val remoteNewsDataSource: RemoteNewsDataSource,
) : NewsRepository {
    override suspend fun getPopularNews(daysPeriod: Int): Flow<MostPopularApiResponse> {
        return remoteNewsDataSource.getPopularNews(daysPeriod)
    }
}
