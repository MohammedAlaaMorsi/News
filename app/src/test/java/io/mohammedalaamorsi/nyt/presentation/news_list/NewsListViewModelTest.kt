package io.mohammedalaamorsi.nyt.presentation.news_list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mohammedalaamorsi.nyt.R
import io.mohammedalaamorsi.nyt.data.models.Media
import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.domain.usecase.GetPopularNewsUseCase
import io.mohammedalaamorsi.nyt.presentation.states.NewsUiState
import io.mohammedalaamorsi.nyt.presentation.states.events.NewsEvent
import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import io.mohammedalaamorsi.nyt.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {
    private lateinit var getPopularNewsUseCase: GetPopularNewsUseCase
    private lateinit var dispatchersProvider: DispatchersProvider
    private lateinit var viewModel: NewsListViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val mockNewsItems =
        listOf(
            Result(
                abstract = "Test article 1 abstract",
                adxKeywords = "test, news",
                assetId = 1L,
                byline = "By Test Author 1",
                column = null,
                desFacet = listOf("test"),
                etaId = 1,
                geoFacet = listOf("US"),
                id = 1L,
                media =
                    listOf(
                        Media(
                            approvedForSyndication = 1,
                            caption = "Test caption",
                            copyright = "Test copyright",
                            mediaMetadata = emptyList(),
                            subtype = "photo",
                            type = "image",
                        ),
                    ),
                nytdsection = "Test Section",
                orgFacet = listOf("Test Org"),
                perFacet = listOf("Test Person"),
                publishedDate = "2025-01-01",
                section = "Technology",
                source = "The New York Times",
                subsection = "",
                title = "Test Article 1",
                type = "Article",
                updated = "2025-01-01 12:00:00",
                uri = "nyt://article/test1",
                url = "https://www.nytimes.com/test1",
            ),
            Result(
                abstract = "Test article 2 abstract",
                adxKeywords = "test, news",
                assetId = 2L,
                byline = "By Test Author 2",
                column = null,
                desFacet = listOf("test"),
                etaId = 2,
                geoFacet = listOf("US"),
                id = 2L,
                media = emptyList(),
                nytdsection = "Test Section",
                orgFacet = listOf("Test Org"),
                perFacet = listOf("Test Person"),
                publishedDate = "2025-01-02",
                section = "Sports",
                source = "The New York Times",
                subsection = "",
                title = "Test Article 2",
                type = "Article",
                updated = "2025-01-02 12:00:00",
                uri = "nyt://article/test2",
                url = "https://www.nytimes.com/test2",
            ),
        )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getPopularNewsUseCase = mockk()
        dispatchersProvider =
            mockk {
                coEvery { io } returns testDispatcher
                coEvery { main } returns testDispatcher
            }
    }

    @Test
    fun `initial state should be Loading and then fetch news automatically`() =
        runTest {
            // Given
            val expectedResponse =
                MostPopularApiResponse(
                    copyright = "Copyright",
                    numResults = 2,
                    results = mockNewsItems,
                    status = "OK",
                )
            coEvery { getPopularNewsUseCase.invoke(7) } returns flowOf(expectedResponse)

            // When
            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Result::class.java)
                val resultState = currentState as NewsUiState.Result
                assertThat(resultState.data).hasSize(2)
                assertThat(resultState.data[0].title).isEqualTo("Test Article 1")
                assertThat(resultState.data[1].title).isEqualTo("Test Article 2")
            }
        }

    @Test
    fun `onEvent FetchNews should update state to Result when success`() =
        runTest {
            // Given
            val daysPeriod = 30
            val expectedResponse =
                MostPopularApiResponse(
                    copyright = "Copyright",
                    numResults = 1,
                    results = listOf(mockNewsItems.first()),
                    status = "OK",
                )
            coEvery { getPopularNewsUseCase.invoke(any()) } returns flowOf(expectedResponse)

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // When
            viewModel.onEvent(NewsEvent.FetchNews(daysPeriod))
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Result::class.java)
                val resultState = currentState as NewsUiState.Result
                assertThat(resultState.data).hasSize(1)
                assertThat(resultState.data.first().title).isEqualTo("Test Article 1")
            }
        }

    @Test
    fun `onEvent FetchNews should update state to Error when exception occurs`() =
        runTest {
            // Given
            val errorMessage = "Network error"
            // Mock the initial call from init block to return a flow that throws
            coEvery { getPopularNewsUseCase.invoke(7) } returns
                flow {
                    throw RuntimeException(errorMessage)
                }

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // Then - verify that the error state is set from the init call
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Error::class.java)
                val errorState = currentState as NewsUiState.Error
                assertThat(errorState.errorMessage).isInstanceOf(UiText.StringResource::class.java)
                val stringResource = errorState.errorMessage as UiText.StringResource
                assertThat(stringResource.resId).isEqualTo(R.string.error_fetching_news)
                assertThat(stringResource.args).contains(errorMessage)
            }
        }

    @Test
    fun `should not update state when response has empty results`() =
        runTest {
            // Given
            val daysPeriod = 7
            val emptyResponse =
                MostPopularApiResponse(
                    copyright = "Copyright",
                    numResults = 0,
                    results = emptyList(),
                    status = "OK",
                )
            coEvery { getPopularNewsUseCase.invoke(daysPeriod) } returns flowOf(emptyResponse)

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)

            // When
            viewModel.onEvent(NewsEvent.FetchNews(daysPeriod))
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                // State should remain Loading since empty results don't update the state
                assertThat(currentState).isInstanceOf(NewsUiState.Loading::class.java)
            }
        }

    @Test
    fun `effects should remain empty when no errors occur`() =
        runTest {
            // Given
            val expectedResponse =
                MostPopularApiResponse(
                    copyright = "Copyright",
                    numResults = 1,
                    results = listOf(mockNewsItems.first()),
                    status = "OK",
                )
            coEvery { getPopularNewsUseCase.invoke(any()) } returns flowOf(expectedResponse)

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // Then
            viewModel.effects.test {
                // Should not emit any effects for successful operations
                expectNoEvents()
            }
        }

    @Test
    fun `multiple fetch events should work correctly`() =
        runTest {
            // Given
            val responses =
                listOf(
                    MostPopularApiResponse("Copyright", 1, listOf(mockNewsItems[0]), "OK"),
                    MostPopularApiResponse("Copyright", 1, listOf(mockNewsItems[1]), "OK"),
                )

            coEvery { getPopularNewsUseCase.invoke(7) } returns flowOf(responses[0])
            coEvery { getPopularNewsUseCase.invoke(30) } returns flowOf(responses[1])

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // When
            viewModel.onEvent(NewsEvent.FetchNews(30))
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Result::class.java)
                val resultState = currentState as NewsUiState.Result
                assertThat(resultState.data.first().title).isEqualTo("Test Article 2")
            }
        }
}
