package com.github.goutarouh.waterworksbureauclone.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = MainBlue,
    primaryVariant = SubBlue,
    background = White
)

@Composable
fun WaterworksBureauCloneTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    context: @Composable () -> Unit
) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        content = context
    )
}