package com.github.goutarouh.waterworksbureauclone

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.goutarouh.waterworksbureauclone.bottombar.WaterworksBureauBottomBar
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WaterworksBureauApp() {

    val navController = rememberAnimatedNavController()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        WaterworksBureauNavigation(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
        WaterworksBureauBottomBar(
            onItemClicked = { bottomNavItem ->
                navController.navigate(route = bottomNavItem.toNav().route)
            },
            modifier = Modifier.align(alignment = Alignment.BottomCenter)
        )
    }
}