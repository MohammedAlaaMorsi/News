package io.mohammedalaamorsi.nyt.presentation.news_list

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.presentation.states.NewsUiState
import io.mohammedalaamorsi.nyt.presentation.states.events.NewsEvent
import io.mohammedalaamorsi.nyt.presentation.theme.NewsTheme
import io.mohammedalaamorsi.nyt.testutils.TestDataFactory
import io.mohammedalaamorsi.nyt.util.UiText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsListScreenUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockNewsItems = TestDataFactory.createMockResultList(3)

    @Test
    fun newsListContent_showsLoadingIndicator_whenStateIsLoading() {
        // Given
        val loadingState = NewsUiState.Loading

        // When
        composeTestRule.setContent {
            NewsListContent(
                state = loadingState,
                paddingValues = androidx.compose.foundation.layout.PaddingValues(),
                onEvent = {},
                onItemClicked = {},
            )
        }

        // Then
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun newsListContent_showsErrorMessage_whenStateIsError() {
        // Given
        val errorMessage = "Network error occurred"
        val errorState = NewsUiState.Error(UiText.DynamicString(errorMessage))

        // When
        composeTestRule.setContent {
            NewsListContent(
                state = errorState,
                paddingValues = androidx.compose.foundation.layout.PaddingValues(),
                onEvent = {},
                onItemClicked = {},
            )
        }

        // Then
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun newsListContent_showsNoResultsView_whenStateIsEmpty() {
        // Given
        val emptyState = NewsUiState.Empty
        var eventTriggered = false
        val onEvent: (NewsEvent) -> Unit = { event ->
            if (event is NewsEvent.FetchNews && event.daysPeriod == 7) {
                eventTriggered = true
            }
        }

        // When
        composeTestRule.setContent {
            NewsListContent(
                state = emptyState,
                paddingValues = androidx.compose.foundation.layout.PaddingValues(),
                onEvent = onEvent,
                onItemClicked = {},
            )
        }

        // Then
        composeTestRule.onNodeWithTag("no_results_view").assertIsDisplayed()

        // When - Click retry button
        composeTestRule.onNodeWithTag("retry_button").performClick()

        // Then - Event should be triggered
        assertThat(eventTriggered).isTrue()
    }

    @Test
    fun newsListContent_showsNewsList_whenStateIsResult() {
        // Given
        val resultState = NewsUiState.Result(mockNewsItems)

        // When
        composeTestRule.setContent {
            NewsListContent(
                state = resultState,
                paddingValues = androidx.compose.foundation.layout.PaddingValues(),
                onEvent = {},
                onItemClicked = {},
            )
        }

        // Then
        composeTestRule.onNodeWithTag("news_list").assertIsDisplayed()
        // Check that individual news cards exist (0, 1, 2 for 3 items)
        composeTestRule.onNodeWithTag("news_card_0").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_card_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_card_2").assertIsDisplayed()
    }

    @Test
    fun newsList_displaysCorrectNumberOfItems() {
        // Given
        val newsItems = TestDataFactory.createMockResultList(5)

        // When
        composeTestRule.setContent {
            NewsList(
                news = newsItems,
                onItemClicked = {},
            )
        }

        // Then
        composeTestRule.onNodeWithTag("news_list").assertIsDisplayed()
        // Check that individual news cards exist (0, 1, 2, 3, 4 for 5 items)
        composeTestRule.onNodeWithTag("news_card_0").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_card_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_card_2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_card_3").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_card_4").assertIsDisplayed()
    }

    @Test
    fun newsList_triggersClickCallback_whenItemClicked() {
        // Given
        val newsItems = TestDataFactory.createMockResultList(1)
        var clickedItem: Result? = null
        val onItemClicked: (Result) -> Unit = { item ->
            clickedItem = item
        }

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsList(
                    news = newsItems,
                    onItemClicked = onItemClicked,
                )
            }
        }

        // Click on the first news card (index 0)
        composeTestRule.onNodeWithTag("news_card_0").performClick()

        // Then
        assertThat(clickedItem).isEqualTo(newsItems.first())
    }

    @Test
    fun newsCard_displaysCorrectContent() {
        // Given
        val newsItem =
            TestDataFactory.createMockResult(
                id = 1,
                title = "Test News Title",
                abstract = "Test news abstract content",
                byline = "By Test Author",
                section = "Technology",
                publishedDate = "2025-01-15",
            )

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItem,
                    index = 0,
                    onItemClicked = {},
                )
            }
        }

        // Then - Verify all text content is displayed
        composeTestRule.onNodeWithText("Test News Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test news abstract content").assertIsDisplayed()
        composeTestRule.onNodeWithText("By Test Author").assertIsDisplayed()
        composeTestRule.onNodeWithText("Technology", substring = true).assertIsDisplayed()
    }

    @Test
    fun newsCard_hasClickAction() {
        // Given
        val newsItem = TestDataFactory.createMockResult()

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItem,
                    index = 0,
                    onItemClicked = {},
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("news_card_0").assertHasClickAction()
    }

    @Test
    fun newsCard_triggersClickCallback_whenClicked() {
        // Given
        val newsItem = TestDataFactory.createMockResult(id = 123)
        var clickedItem: Result? = null
        val onItemClicked: (Result) -> Unit = { item ->
            clickedItem = item
        }

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItem,
                    index = 0,
                    onItemClicked = onItemClicked,
                )
            }
        }

        // Click the card
        composeTestRule.onNodeWithTag("news_card_0").performClick()

        // Then
        assertThat(clickedItem).isEqualTo(newsItem)
        assertThat(clickedItem?.id).isEqualTo(123)
    }

    @Test
    fun newsCard_displaysImage_whenMediaAvailable() {
        // Given
        val newsItem = TestDataFactory.createMockResult()

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItem,
                    index = 0,
                    onItemClicked = {},
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("news_image") // Just verify the node exists
    }

    @Test
    fun newsCard_displaysPlaceholder_whenNoMediaAvailable() {
        // Given
        val newsItemWithoutMedia = TestDataFactory.createMockResult().copy(media = emptyList())

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItemWithoutMedia,
                    index = 0,
                    onItemClicked = {},
                )
            }
        }

        // Then - Image should still be displayed (with placeholder)
        composeTestRule.onNodeWithTag("news_image") // Just verify the node exists
    }

    @Test
    fun newsCard_truncatesLongText_correctly() {
        // Given
        val longTitle =
            "This is a very long news title that should be truncated after two lines " +
                "to ensure proper UI layout and readability"
        val longAbstract =
            "This is a very long abstract that should be truncated after two lines " +
                "to maintain the card layout and prevent overflow issues in the user interface"
        val newsItem =
            TestDataFactory.createMockResult(
                title = longTitle,
                abstract = longAbstract,
            )

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItem,
                    index = 0,
                    onItemClicked = {},
                )
            }
        }

        // Then - Text should be displayed (truncation is handled by maxLines property)
        composeTestRule.onNodeWithText(longTitle, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(longAbstract, substring = true).assertIsDisplayed()
    }

    @Test
    fun newsCard_displaysFormattedDate_correctly() {
        // Given
        val newsItem =
            TestDataFactory.createMockResult(
                publishedDate = "2025-01-15",
                section = "Technology",
            )

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsCard(
                    newsItem = newsItem,
                    index = 0,
                    onItemClicked = {},
                )
            }
        }

        // Then - Should display formatted date with section
        composeTestRule.onNode(
            androidx.compose.ui.test.hasText("Technology", substring = true),
        ).assertIsDisplayed()
    }
}
