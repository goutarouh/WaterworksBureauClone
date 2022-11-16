package com.github.goutarouh.waterworksbureauclone

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

enum class MainNavDest(val route: String) {
    HOME("home"),
    NOTIFICATION("notification"),
    PAYING("paying"),
    USAGE_HISTORY("usage_history"),
    MENU("menu")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WaterworksBureauNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = MainNavDest.HOME.route,
        modifier = modifier
    ) {
        composableWithAnimation(
            route = MainNavDest.HOME.route,
        ) {
            HomeScreen()
        }
        composableWithAnimation(
            route = MainNavDest.NOTIFICATION.route
        ) {
            NotificationScreen()
        }
        composableWithAnimation(
            route = MainNavDest.PAYING.route
        ) {
            PayingScreen()
        }
        composableWithAnimation(
            route = MainNavDest.USAGE_HISTORY.route
        ) {
            UsageHistoryScreen()
        }
        composableWithAnimation(
            route = MainNavDest.MENU.route
        ) {
            MenuScreen()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableWithAnimation(
    route: String,
    screen: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideIn(
                animationSpec = TweenSpec(durationMillis = 1000, delay = 200, easing = CubicBezierEasing(0.5f, 0.7f, 0.7f, 1f)),
                initialOffset = { f -> IntOffset(x = 0, y = f.height / 50) }
            ) + fadeIn(
                tween(delayMillis = 300)
            )
        },
        exitTransition = {
            fadeOut(tween(0, 0))
        }
    ) {
        screen()
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "HomeScreen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun NotificationScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "NotificationScreen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun PayingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "PayingScreen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun UsageHistoryScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "UsageHistoryScreen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun MenuScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "MenuScreen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}