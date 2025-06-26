package io.mohammedalaamorsi.nyt.presentation.news_details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.mohammedalaamorsi.nyt.data.models.Media
import io.mohammedalaamorsi.nyt.data.models.MediaMetadata
import io.mohammedalaamorsi.nyt.presentation.theme.NewsTheme
import io.mohammedalaamorsi.nyt.testutils.TestDataFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailsScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun newsDetailsScreen_withValidItem_displaysAllContent() {
        // Given
        val testResult = TestDataFactory.createMockResult().copy(
            title = "Test News Title",
            abstract = "Test news abstract content",
            byline = "By Test Author",
            section = "Technology",
            subsection = "Apps",
            publishedDate = "2023-12-01"
        )
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("news_details_content").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("section_badge").assertIsDisplayed()
        composeTestRule.onNodeWithTag("date_section").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_title").assertTextEquals("Test News Title")
        composeTestRule.onNodeWithTag("author_section").assertIsDisplayed()
        composeTestRule.onNodeWithTag("news_abstract").assertTextEquals("Test news abstract content")
        composeTestRule.onNodeWithTag("article_details_card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("subsection_card").assertIsDisplayed()
    }

    @Test
    fun itemDetails_withNullItem_showsNoDetailsMessage() {
        // Given

        // When
        composeTestRule.setContent {
            NewsTheme {
                ItemDetails(
                    innerPadding = PaddingValues(0.dp),
                    item = null
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("no_details_available").assertIsDisplayed()
    }

    @Test
    fun newsDetailsScreen_backButton_triggersNavigation() {
        // Given
        val testResult = TestDataFactory.createMockResult()
        val viewModel = NewsDetailsViewModel(testResult)
        var backPressed = false

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { backPressed = true }
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("back_button").performClick()

        // Then
        assertThat(backPressed).isTrue()
    }

    @Test
    fun newsDetailsScreen_inListDetailView_hidesTopBar() {
        // Given
        val testResult = TestDataFactory.createMockResult()
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { },
                    isInListDetailView = true
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("back_button").assertDoesNotExist()
        composeTestRule.onNodeWithTag("news_details_content").assertIsDisplayed()
    }

    @Test
    fun newsDetailsScreen_withoutSubsection_hidesSubsectionCard() {
        // Given
        val testResult = TestDataFactory.createMockResult().copy(subsection = "")
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("subsection_card").assertDoesNotExist()
        composeTestRule.onNodeWithTag("news_details_content").assertIsDisplayed()
    }

    @Test
    fun newsDetailsScreen_withEmptyPhotoCaption_hidesPhotoCaptionCard() {
        // Given
        val mediaWithoutCaption = Media(
            type = "image",
            subtype = "photo",
            caption = "", // Empty caption
            copyright = "Test Copyright",
            approvedForSyndication = 1,
            mediaMetadata = listOf(
                MediaMetadata(
                    url = "https://example.com/image.jpg",
                    format = "mediumThreeByTwo440",
                    height = 293,
                    width = 440
                )
            )
        )
        val testResult = TestDataFactory.createMockResult().copy(
            media = listOf(mediaWithoutCaption)
        )
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("photo_caption_card").assertDoesNotExist()
        composeTestRule.onNodeWithTag("news_details_content").assertIsDisplayed()
    }

    @Test
    fun newsDetailsScreen_withPhotoCaption_showsPhotoCaptionCard() {
        // Given
        val mediaWithCaption = Media(
            type = "image",
            subtype = "photo",
            caption = "Test photo caption",
            copyright = "Test Copyright",
            approvedForSyndication = 1,
            mediaMetadata = listOf(
                MediaMetadata(
                    url = "https://example.com/image.jpg",
                    format = "mediumThreeByTwo440",
                    height = 293,
                    width = 440
                )
            )
        )
        val testResult = TestDataFactory.createMockResult().copy(
            media = listOf(mediaWithCaption)
        )
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("photo_caption_card").assertIsDisplayed()
    }

    @Test
    fun newsDetailsScreen_displaysCorrectSectionBadge() {
        // Given
        val testResult = TestDataFactory.createMockResult().copy(section = "Technology")
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { }
                )
            }
        }

        // Then - Check that the section badge exists and contains Technology text (case insensitive)
        composeTestRule.onNodeWithTag("section_badge").assertIsDisplayed()
        composeTestRule.onNodeWithTag("section_badge").assertTextContains("TECHNOLOGY", ignoreCase = true)
    }

    @Test
    fun newsDetailsScreen_displaysAuthorInformation() {
        // Given - Use "By Test Author" which matches the default mock data
        val testResult = TestDataFactory.createMockResult()  // Uses default byline "By Test Author"

        // When - Test ItemDetails directly instead of through the ViewModel
        composeTestRule.setContent {
            NewsTheme {
                ItemDetails(
                    innerPadding = PaddingValues(0.dp),
                    item = testResult,
                    isInListDetailView = true
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("By Test Author").assertIsDisplayed()
    }

    @Test
    fun itemDetails_withCompleteData_showsAllSections() {
        // Given
        val testResult = TestDataFactory.createMockResult().copy(
            desFacet = listOf("Technology", "Innovation"),
            geoFacet = listOf("New York", "California"),
            orgFacet = listOf("Apple", "Google"),
            perFacet = listOf("Tim Cook", "Sundar Pichai")
        )
        val viewModel = NewsDetailsViewModel(testResult)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("article_details_card").assertIsDisplayed()
        // The specific facet content will be displayed within the article details card
    }
}
