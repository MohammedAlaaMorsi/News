package io.mohammedalaamorsi.nyt.integration

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mohammedalaamorsi.nyt.R
import io.mohammedalaamorsi.nyt.domain.usecase.GetPopularNewsUseCase
import io.mohammedalaamorsi.nyt.presentation.news_list.NewsListViewModel
import io.mohammedalaamorsi.nyt.presentation.states.NewsUiState
import io.mohammedalaamorsi.nyt.presentation.states.events.NewsEvent
import io.mohammedalaamorsi.nyt.testutils.TestDataFactory
import io.mohammedalaamorsi.nyt.testutils.TestDispatchersProvider
import io.mohammedalaamorsi.nyt.util.UiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Integration tests that verify the interaction between UseCase and ViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NewsListIntegrationTest {
    private lateinit var getPopularNewsUseCase: GetPopularNewsUseCase
    private lateinit var dispatchersProvider: TestDispatchersProvider
    private lateinit var viewModel: NewsListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        getPopularNewsUseCase = mockk()
        dispatchersProvider = TestDispatchersProvider(testDispatcher)
    }

    @Test
    fun `complete flow from usecase to viewmodel should work correctly`() =
        runTest(testDispatcher) {
            // Given
            val mockResults = TestDataFactory.createMockResultList(3)
            val mockResponse = TestDataFactory.createMockApiResponse(mockResults)
            coEvery { getPopularNewsUseCase.invoke(7) } returns flowOf(mockResponse)

            // When
            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Result::class.java)
                val resultState = currentState as NewsUiState.Result
                assertThat(resultState.data).hasSize(3)
                assertThat(resultState.data.map { it.title }).containsExactly(
                    "Test Article 1",
                    "Test Article 2",
                    "Test Article 3",
                )
            }
        }

    @Test
    fun `error handling should propagate correctly through layers`() =
        runTest(testDispatcher) {
            // Given
            val errorMessage = "Network connection failed"
            coEvery { getPopularNewsUseCase.invoke(any()) } returns
                flow {
                    throw RuntimeException(errorMessage)
                }

            // When
            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // Then
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
    fun `different days periods should be handled correctly end-to-end`() =
        runTest(testDispatcher) {
            // Given
            val daysPeriods = listOf(1, 7, 30)
            val responses =
                daysPeriods.mapIndexed { index, days ->
                    val results = TestDataFactory.createMockResultList(index + 1)
                    TestDataFactory.createMockApiResponse(results)
                }

            daysPeriods.forEachIndexed { index, days ->
                coEvery { getPopularNewsUseCase.invoke(days) } returns flowOf(responses[index])
            }

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // When & Then - Test each period
            daysPeriods.forEachIndexed { index, days ->
                viewModel.onEvent(NewsEvent.FetchNews(days))
                advanceUntilIdle()

                viewModel.state.test {
                    val currentState = awaitItem()
                    assertThat(currentState).isInstanceOf(NewsUiState.Result::class.java)
                    val resultState = currentState as NewsUiState.Result
                    assertThat(resultState.data).hasSize(index + 1)
                }
            }
        }

    @Test
    fun `empty results should not change state from loading`() =
        runTest(testDispatcher) {
            // Given
            val emptyResponse = TestDataFactory.createMockApiResponse(emptyList())
            coEvery { getPopularNewsUseCase.invoke(any()) } returns flowOf(emptyResponse)

            // When
            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            viewModel.onEvent(NewsEvent.FetchNews(7))
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Loading::class.java)
            }
        }

    @Test
    fun `multiple rapid fetch events should be handled gracefully`() =
        runTest(testDispatcher) {
            // Given
            val responses =
                (1..5).map { index ->
                    val results = TestDataFactory.createMockResultList(index)
                    TestDataFactory.createMockApiResponse(results)
                }

            // Mock the initial call (7) from ViewModel init
            coEvery { getPopularNewsUseCase.invoke(7) } returns flowOf(responses[0])

            responses.forEachIndexed { index, response ->
                coEvery { getPopularNewsUseCase.invoke(index + 1) } returns flowOf(response)
            }

            viewModel = NewsListViewModel(getPopularNewsUseCase, dispatchersProvider)
            advanceUntilIdle()

            // When - Fire multiple events rapidly
            repeat(5) { index ->
                viewModel.onEvent(NewsEvent.FetchNews(index + 1))
            }
            advanceUntilIdle()

            // Then - Should handle the last event correctly
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState).isInstanceOf(NewsUiState.Result::class.java)
                val resultState = currentState as NewsUiState.Result
                // Should show results from the last successful fetch
                assertThat(resultState.data).isNotEmpty()
            }
        }
}
