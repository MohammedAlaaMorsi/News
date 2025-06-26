package io.mohammedalaamorsi.nyt.presentation.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mohammedalaamorsi.nyt.R
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.presentation.news_details.NewsDetailsScreen
import io.mohammedalaamorsi.nyt.presentation.news_details.NewsDetailsViewModel
import io.mohammedalaamorsi.nyt.presentation.news_list.NewsListScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveNewsScreen() {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Result>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                NewsListScreen(
                    onNewsClicked = { item ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                item
                            )
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let { newsItem ->
                    val newsDetailsViewModel = koinViewModel<NewsDetailsViewModel>(
                        key = "newsDetail_${newsItem.id}" // Use unique key based on news item ID
                    ) {
                        parametersOf(newsItem)
                    }
                    
                    val isDualPane = scaffoldNavigator.scaffoldDirective.maxHorizontalPartitions > 1
                    
                    NewsDetailsScreen(
                        viewModel = newsDetailsViewModel,
                        isInListDetailView = isDualPane,
                        onNavigateBack = {
                            scope.launch {
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.select_a_news_article_to_read),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}
