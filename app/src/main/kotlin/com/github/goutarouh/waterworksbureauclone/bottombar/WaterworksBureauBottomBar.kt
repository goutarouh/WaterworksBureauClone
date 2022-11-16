package com.github.goutarouh.waterworksbureauclone.bottombar

import androidx.compose.animation.Animatable
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.goutarouh.waterworksbureauclone.R
import com.github.goutarouh.waterworksbureauclone.ui.theme.MainBlue
import kotlin.math.abs

enum class BottomNavItem(
    val stringRes: Int,
    val imageVector: ImageVector,
    val order: Int
) {
    HOME(R.string.home, Icons.Filled.Home,0),
    NOTIFICATION(R.string.notification, Icons.Filled.Notifications, 1),
    PAY(R.string.pay, Icons.Filled.Payment, 2),
    HISTORY(R.string.history, Icons.Filled.History, 3),
    MENU(R.string.menu, Icons.Filled.MoreHoriz, 4)
}

@Composable
fun WaterworksBureauBottomBar() {
    val items = BottomNavItem.values().sortedBy { it.order }
    var preSelectedIndex by remember { mutableStateOf(0) }
    var selectedIndex by remember { mutableStateOf(0) }

    val indicatorAngleAnimSpec = TweenSpec<Float>(durationMillis = 500)
    val dropLetAngle = remember { Animatable(0f) }
    LaunchedEffect(selectedIndex) {
        when {
            preSelectedIndex < selectedIndex -> dropLetAngle.animateTo(-20f, indicatorAngleAnimSpec)
            preSelectedIndex > selectedIndex -> dropLetAngle.animateTo(20f, indicatorAngleAnimSpec)
        }
        dropLetAngle.animateTo(0f, indicatorAngleAnimSpec)
    }

    val tabColors = remember(items.size) {
        List(items.size) { i ->
            Animatable(Color.White)
        }
    }
    tabColors.forEachIndexed { index, animatable ->
        val selected = index == selectedIndex
        val target = if (selected) MainBlue else Color.White
        LaunchedEffect(target) {
            val diffPos = abs(preSelectedIndex - selectedIndex)
            val delay = if (preSelectedIndex == selectedIndex) {
                0
            } else {
                200 + if (selected) diffPos * 120 else 0
            }
            animatable.animateTo(target, TweenSpec(durationMillis = 200, delay = delay))
        }
    }

    LaunchedEffect(selectedIndex) {
        when {
            preSelectedIndex < selectedIndex -> dropLetAngle.animateTo(-20f, indicatorAngleAnimSpec)
            preSelectedIndex > selectedIndex -> dropLetAngle.animateTo(20f, indicatorAngleAnimSpec)
        }
        dropLetAngle.animateTo(0f, indicatorAngleAnimSpec)
    }

    val dropLetAlpha = remember { Animatable(1f) }
    LaunchedEffect(selectedIndex) {
        if (preSelectedIndex == selectedIndex) return@LaunchedEffect
        val diffPos = abs(preSelectedIndex - selectedIndex)
        if (diffPos <= 1) return@LaunchedEffect
        val durationMills = diffPos * 150
        dropLetAlpha.animateTo(0f, TweenSpec(durationMillis = durationMills, delay = 200))
        dropLetAlpha.animateTo(1f, TweenSpec(durationMillis = 0, delay = 200))
    }


    WaterWorksBureauBottomNavLayout(
        selectedIndex = selectedIndex,
        itemCount = items.size,
        dropletIndicator = { Droplet(dropLetAngle.value, dropLetAlpha.value) }
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            WaterWorksBureauBottomNavigationIem(
                icon = {
                    Icon(
                        imageVector = item.imageVector,
                        tint = tabColors[index].value,
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = stringResource(item.stringRes),
                        fontSize = 12.sp,
                        color = tabColors[index].value
                    )
                },
                selected = selected,
                onSelected = {
                    preSelectedIndex = selectedIndex
                    selectedIndex = index
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

    val indicatorMovingAnimSpec = TweenSpec<Float>(
        durationMillis = 1000
    )

    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, indicatorMovingAnimSpec)
    }

    Layout(
        modifier = Modifier
            .height(BottomNavHeight)
            .background(color = MaterialTheme.colors.primary),
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