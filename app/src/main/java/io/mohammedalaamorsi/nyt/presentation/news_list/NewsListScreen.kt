package io.mohammedalaamorsi.nyt.presentation.news_list

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.mohammedalaamorsi.nyt.R
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.presentation.states.NewsUiState
import io.mohammedalaamorsi.nyt.presentation.states.effects.Effect
import io.mohammedalaamorsi.nyt.presentation.states.events.NewsEvent
import io.mohammedalaamorsi.nyt.presentation.ui.ErrorMessage
import io.mohammedalaamorsi.nyt.presentation.ui.LoadingIndicator
import io.mohammedalaamorsi.nyt.presentation.ui.NoResultsFoundView
import io.mohammedalaamorsi.nyt.util.formatDetailDate
import org.koin.androidx.compose.koinViewModel


@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel = koinViewModel(),
    onNewsClicked: (Result) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { paddingValues ->
        NewsListContent(
            state = state,
            paddingValues = paddingValues,
            onEvent = viewModel::onEvent
        ) {
            onNewsClicked(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                is Effect.ShowSnackbarResource -> {
                    snackbarHostState.showSnackbar(
                        message = it.messageRes,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}

@Composable
fun NewsListContent(
    state: NewsUiState,
    paddingValues: PaddingValues,
    onEvent: (NewsEvent) -> Unit,
    onItemClicked: (Result) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {


        when (state) {
            is NewsUiState.Error -> {
                ErrorMessage(state.errorMessage)
            }

            is NewsUiState.Loading -> {
                LoadingIndicator()
            }

            is NewsUiState.Result -> {
                NewsList(
                    news = state.data,
                    onItemClicked = onItemClicked
                )
            }

            NewsUiState.Empty -> {
                NoResultsFoundView {
                    onEvent(NewsEvent.FetchNews(7))
                }
            }
        }

    }
}

@Composable
fun NewsList(
    news: List<Result>,
    onItemClicked: (Result) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(news) { item ->
            NewsCard(
                newsItem = item,
                onItemClicked = { onItemClicked(it) }
            )
        }
    }
}

@Composable
fun NewsCard(
    newsItem: Result,
    onItemClicked: (Result) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onItemClicked(newsItem) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Image at the start
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        newsItem.media.firstOrNull()?.mediaMetadata?.getOrNull(2)?.url
                            ?: newsItem.media.firstOrNull()?.mediaMetadata?.firstOrNull()?.url
                    )
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = newsItem.media.firstOrNull()?.caption,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "${newsItem.publishedDate.formatDetailDate()} • ${newsItem.section}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = newsItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = newsItem.abstract,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = newsItem.byline,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
