package com.github.goutarouh.waterworksbureauclone.bottombar

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId

@Composable
fun WaterworksBureauBottomBar() {
    val list = listOf("1", "2", "3", "4")
    val selectedIndex = remember { mutableStateOf(0) }
    WaterWorksBureauBottomNavLayout(
        selectedIndex = selectedIndex.value,
        itemCount = list.size,
        dropletIndicator = { Droplet() }
    ) {
        list.forEachIndexed { index, s ->
            val selected = index == selectedIndex.value
            val tint = if (selected) MaterialTheme.colors.primary else Color.White
            WaterWorksBureauBottomNavigationIem(
                text = {
                    Text(text = "item$s", color = tint)
                },
                selected = selected,
                onSelected = {
                    selectedIndex.value = index
                }
            )
        }
    }
}

@Composable
fun WaterWorksBureauBottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    dropletIndicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex)
    }

    Layout(
        modifier = Modifier.height(BottomNavHeight).background(color = MaterialTheme.colors.primary),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = dropletIndicator)
        }
    ) { measurables, constraints ->

        val itemWidth = constraints.maxWidth / itemCount
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val itemPlaceables = measurables
            .filterNot { it == indicatorMeasurable }
            .map { measurable ->
                measurable.measure(constraints.copy(
                    minWidth = itemWidth,
                    maxWidth = itemWidth
                ))
            }

        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = itemWidth,
                maxWidth = itemWidth
            )
        )

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            val indicatorLeft = indicatorIndex.value * itemWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}


private val BottomNavHeight = 60.dp //56