package io.mohammedalaamorsi.nyt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun RootNavHost(modifier: Modifier = Modifier) {

    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screens.NewsList,
        modifier = modifier,
    ) {
        composable<Screens.NewsList> {

        }
        composable<Screens.NewsDetails> {

        }


    }
}
