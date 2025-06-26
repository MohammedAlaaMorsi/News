package io.mohammedalaamorsi.nyt.presentation.news_details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mohammedalaamorsi.nyt.data.models.Media
import io.mohammedalaamorsi.nyt.data.models.MediaMetadata
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.presentation.states.NewsDetailsUiState
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NewsDetailsViewModelTest {
    private val mockMediaMetadata =
        listOf(
            MediaMetadata(
                format = "mediumThreeByTwo210",
                height = 140,
                url = "https://static01.nyt.com/images/test-medium.jpg",
                width = 210,
            ),
            MediaMetadata(
                format = "superJumbo",
                height = 1365,
                url = "https://static01.nyt.com/images/test-super.jpg",
                width = 2048,
            ),
        )

    private val mockMedia =
        listOf(
            Media(
                approvedForSyndication = 1,
                caption = "A test image caption for the news article",
                copyright = "The New York Times",
                mediaMetadata = mockMediaMetadata,
                subtype = "photo",
                type = "image",
            ),
        )

    private val mockNewsItem =
        Result(
            abstract = "This is a test abstract for a news article that provides a brief summary of the content.",
            adxKeywords = "test, news, article, technology",
            assetId = 100001234567890L,
            byline = "By John Doe",
            column = "Technology Review",
            desFacet = listOf("Technology", "Innovation", "Science"),
            etaId = 42,
            geoFacet = listOf("United States", "California", "Silicon Valley"),
            id = 100001234567890L,
            media = mockMedia,
            nytdsection = "Technology",
            orgFacet = listOf("Apple Inc", "Google LLC", "Microsoft Corporation"),
            perFacet = listOf("Tim Cook", "Sundar Pichai", "Satya Nadella"),
            publishedDate = "2025-01-15",
            section = "Technology",
            source = "The New York Times",
            subsection = "Personal Tech",
            title = "Revolutionary AI Technology Transforms Daily Life",
            type = "Article",
            updated = "2025-01-15 14:30:00",
            uri = "nyt://article/100001234567890",
            url = "https://www.nytimes.com/2025/01/15/technology/ai-transforms-daily-life.html",
        )

    @Test
    fun `initialization should set item in state`() =
        runTest {
            // When
            val viewModel = NewsDetailsViewModel(mockNewsItem)

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState.item).isEqualTo(mockNewsItem)
                assertThat(currentState.item?.title).isEqualTo("Revolutionary AI Technology Transforms Daily Life")
                assertThat(
                    currentState.item?.abstract,
                ).isEqualTo("This is a test abstract for a news article that provides a brief summary of the content.")
                assertThat(currentState.item?.byline).isEqualTo("By John Doe")
                assertThat(currentState.item?.section).isEqualTo("Technology")
                assertThat(currentState.item?.publishedDate).isEqualTo("2025-01-15")
            }
        }

    @Test
    fun `state should contain correct media information`() =
        runTest {
            // When
            val viewModel = NewsDetailsViewModel(mockNewsItem)

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                val media = currentState.item?.media?.firstOrNull()
                assertThat(media).isNotNull()
                assertThat(media?.caption).isEqualTo("A test image caption for the news article")
                assertThat(media?.copyright).isEqualTo("The New York Times")
                assertThat(media?.mediaMetadata).hasSize(2)
                assertThat(media?.mediaMetadata?.get(0)?.format).isEqualTo("mediumThreeByTwo210")
                assertThat(media?.mediaMetadata?.get(1)?.format).isEqualTo("superJumbo")
            }
        }

    @Test
    fun `state should contain correct facet information`() =
        runTest {
            // When
            val viewModel = NewsDetailsViewModel(mockNewsItem)

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                val item = currentState.item
                assertThat(item?.desFacet).containsExactly("Technology", "Innovation", "Science")
                assertThat(item?.geoFacet).containsExactly("United States", "California", "Silicon Valley")
                assertThat(item?.orgFacet).containsExactly("Apple Inc", "Google LLC", "Microsoft Corporation")
                assertThat(item?.perFacet).containsExactly("Tim Cook", "Sundar Pichai", "Satya Nadella")
            }
        }

    @Test
    fun `state should handle item with minimal media`() =
        runTest {
            // Given
            val itemWithoutMedia = mockNewsItem.copy(media = emptyList())

            // When
            val viewModel = NewsDetailsViewModel(itemWithoutMedia)

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState.item).isEqualTo(itemWithoutMedia)
                assertThat(currentState.item?.media).isEmpty()
                assertThat(currentState.item?.title).isEqualTo("Revolutionary AI Technology Transforms Daily Life")
            }
        }

    @Test
    fun `state should handle item with null column`() =
        runTest {
            // Given
            val itemWithNullColumn = mockNewsItem.copy(column = null)

            // When
            val viewModel = NewsDetailsViewModel(itemWithNullColumn)

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                assertThat(currentState.item).isEqualTo(itemWithNullColumn)
                assertThat(currentState.item?.column).isNull()
                assertThat(currentState.item?.title).isEqualTo("Revolutionary AI Technology Transforms Daily Life")
            }
        }

    @Test
    fun `state should contain all required article metadata`() =
        runTest {
            // When
            val viewModel = NewsDetailsViewModel(mockNewsItem)

            // Then
            viewModel.state.test {
                val currentState = awaitItem()
                val item = currentState.item

                // Verify all essential fields are preserved
                assertThat(item?.id).isEqualTo(100001234567890L)
                assertThat(item?.assetId).isEqualTo(100001234567890L)
                assertThat(item?.uri).isEqualTo("nyt://article/100001234567890")
                assertThat(
                    item?.url,
                ).isEqualTo("https://www.nytimes.com/2025/01/15/technology/ai-transforms-daily-life.html")
                assertThat(item?.source).isEqualTo("The New York Times")
                assertThat(item?.nytdsection).isEqualTo("Technology")
                assertThat(item?.subsection).isEqualTo("Personal Tech")
                assertThat(item?.type).isEqualTo("Article")
                assertThat(item?.updated).isEqualTo("2025-01-15 14:30:00")
                assertThat(item?.etaId).isEqualTo(42)
                assertThat(item?.adxKeywords).isEqualTo("test, news, article, technology")
            }
        }

    @Test
    fun `default state should have null item`() {
        // Given
        val defaultState = NewsDetailsUiState()

        // Then
        assertThat(defaultState.item).isNull()
    }

    @Test
    fun `state should be immutable after initialization`() =
        runTest {
            // Given
            val viewModel = NewsDetailsViewModel(mockNewsItem)

            // When & Then
            viewModel.state.test {
                val state = awaitItem()
                assertThat(state.item).isEqualTo(mockNewsItem)

                // Since the ViewModel doesn't emit again, we just verify the single emission
                expectNoEvents()
            }
        }
}
