package io.mohammedalaamorsi.nyt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.mohammedalaamorsi.nyt.data.models.Result
import io.mohammedalaamorsi.nyt.presentation.adaptive.AdaptiveNewsScreen
import io.mohammedalaamorsi.nyt.presentation.news_details.NewsDetailsScreen
import io.mohammedalaamorsi.nyt.presentation.news_details.NewsDetailsViewModel
import io.mohammedalaamorsi.nyt.presentation.news_list.NewsListScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@Composable
fun RootNavHost(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screens.AdaptiveNews,
        modifier = modifier,
    ) {
        composable<Screens.AdaptiveNews> {
            AdaptiveNewsScreen()
        }

        composable<Screens.NewsList> {
            NewsListScreen(
                onNewsClicked = { item ->
                    navHostController.navigate(
                        Screens.NewsDetails(item),
                    )
                },
            )
        }

        composable<Screens.NewsDetails>(
            typeMap = mapOf(typeOf<Result>() to ResultNavType),
        ) { backStackEntry ->
            val newsDetails = backStackEntry.toRoute<Screens.NewsDetails>()

            val newsDetailsViewModel =
                koinViewModel<NewsDetailsViewModel> {
                    parametersOf(newsDetails.item)
                }
            NewsDetailsScreen(
                viewModel = newsDetailsViewModel,
                isInListDetailView = false,
                onNavigateBack = {
                    navHostController.popBackStack()
                },
            )
        }
    }
}
