package io.mohammedalaamorsi.nyt.domain.usecase

import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import io.mohammedalaamorsi.nyt.domain.NewsRepository
import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetPopularNewsUseCase(
    private val newsRepository: NewsRepository,
    private val dispatchersProvider: DispatchersProvider,
) {
    suspend operator fun invoke(daysPeriod: Int): Flow<MostPopularApiResponse> {
        return newsRepository
            .getPopularNews(daysPeriod = daysPeriod)
            .flowOn(dispatchersProvider.io)
    }
}
