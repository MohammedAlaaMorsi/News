package io.mohammedalaamorsi.nyt

import io.mohammedalaamorsi.nyt.domain.usecase.GetPopularNewsUseCaseTest
import io.mohammedalaamorsi.nyt.integration.NewsListIntegrationTest
import io.mohammedalaamorsi.nyt.presentation.news_details.NewsDetailsViewModelTest
import io.mohammedalaamorsi.nyt.presentation.news_list.NewsListViewModelTest
import io.mohammedalaamorsi.nyt.util.DispatchersProviderTest
import io.mohammedalaamorsi.nyt.util.UiTextTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite that runs all unit tests for the domain layer and ViewModels
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Domain Layer Tests
    GetPopularNewsUseCaseTest::class,
    
    // ViewModel Tests
    NewsListViewModelTest::class,
    NewsDetailsViewModelTest::class,
    
    // Utility Tests
    DispatchersProviderTest::class,
    UiTextTest::class,
    
    // Integration Tests
    NewsListIntegrationTest::class
)
class DomainAndViewModelTestSuite
