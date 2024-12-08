package com.dragos.brainstorming

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dragos.brainstorming.good_bad_app.GoodBadAppScreen
import com.dragos.brainstorming.island.IslandScreen
import com.dragos.brainstorming.limit.LimitScreen
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Island
@Serializable
object GoodBadApp
@Serializable
object Limits

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                navToIsland = {
                    navController.navigate(Island)
                },
                navToLimits = {
                    navController.navigate(Limits)
                },
                navToGoodBadApp = {
                    navController.navigate(GoodBadApp)
                },
            )
        }
        composable<Island> {
            IslandScreen()
        }
        composable<GoodBadApp> {
            GoodBadAppScreen()
        }
        composable<Limits> {
            LimitScreen()
        }
    }
}