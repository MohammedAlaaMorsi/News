package io.mohammedalaamorsi.nyt.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import io.mohammedalaamorsi.nyt.domain.NewsRepository
import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularNewsUseCaseTest {
    private lateinit var newsRepository: NewsRepository
    private lateinit var dispatchersProvider: DispatchersProvider
    private lateinit var getPopularNewsUseCase: GetPopularNewsUseCase

    @Before
    fun setUp() {
        newsRepository = mockk()
        dispatchersProvider =
            mockk {
                coEvery { io } returns Dispatchers.Unconfined
            }
        getPopularNewsUseCase =
            GetPopularNewsUseCase(
                newsRepository = newsRepository,
                dispatchersProvider = dispatchersProvider,
            )
    }

    @Test
    fun `invoke should return flow from repository with correct dispatcher`() =
        runTest {
            // Given
            val daysPeriod = 7
            val expectedResponse =
                MostPopularApiResponse(
                    copyright = "Copyright (c) 2025 The New York Times Company.",
                    numResults = 2,
                    results = emptyList(),
                    status = "OK",
                )
            coEvery { newsRepository.getPopularNews(daysPeriod) } returns flowOf(expectedResponse)

            // When
            val result = getPopularNewsUseCase.invoke(daysPeriod)

            // Then
            result.test {
                val emission = awaitItem()
                assertThat(emission).isEqualTo(expectedResponse)
                assertThat(emission.status).isEqualTo("OK")
                assertThat(emission.numResults).isEqualTo(2)
                awaitComplete()
            }
        }

    @Test
    fun `invoke should propagate repository errors`() =
        runTest {
            // Given
            val daysPeriod = 30
            val expectedException = RuntimeException("Network error")
            coEvery { newsRepository.getPopularNews(daysPeriod) } throws expectedException

            // When & Then
            try {
                val result = getPopularNewsUseCase.invoke(daysPeriod)
                result.test {
                    awaitItem() // This will throw the exception
                }
            } catch (exception: Exception) {
                assertThat(exception).isEqualTo(expectedException)
            }
        }

    @Test
    fun `invoke should work with different days periods`() =
        runTest {
            // Given
            val daysPeriods = listOf(1, 7, 30)
            val responses =
                daysPeriods.map { days ->
                    MostPopularApiResponse(
                        copyright = "Copyright (c) 2025 The New York Times Company.",
                        numResults = days,
                        results = emptyList(),
                        status = "OK",
                    )
                }

            daysPeriods.forEachIndexed { index, days ->
                coEvery { newsRepository.getPopularNews(days) } returns flowOf(responses[index])
            }

            // When & Then
            daysPeriods.forEachIndexed { index, days ->
                val result = getPopularNewsUseCase.invoke(days)
                result.test {
                    val emission = awaitItem()
                    assertThat(emission.numResults).isEqualTo(days)
                    awaitComplete()
                }
            }
        }
}
