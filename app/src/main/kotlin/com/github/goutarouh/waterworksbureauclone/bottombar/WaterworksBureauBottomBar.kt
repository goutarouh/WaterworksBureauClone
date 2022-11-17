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
import com.github.goutarouh.waterworksbureauclone.MainNavDest
import com.github.goutarouh.waterworksbureauclone.R
import com.github.goutarouh.waterworksbureauclone.ui.theme.MainBlue
import com.github.goutarouh.waterworksbureauclone.ui.theme.SubBlue
import kotlin.math.abs

enum class BottomNavItem(
    val stringRes: Int,
    val imageVector: ImageVector,
    val order: Int
) {
    HOME(R.string.home, Icons.Filled.Home,0),
    NOTIFICATION(R.string.notification, Icons.Filled.Notifications, 1),
    PAYING(R.string.pay, Icons.Filled.Payment, 2),
    USAGE_HISTORY(R.string.history, Icons.Filled.History, 3),
    MENU(R.string.menu, Icons.Filled.MoreHoriz, 4);

    fun toNav(): MainNavDest {
        return when(this) {
            HOME -> MainNavDest.HOME
            NOTIFICATION -> MainNavDest.NOTIFICATION
            PAYING -> MainNavDest.PAYING
            USAGE_HISTORY -> MainNavDest.USAGE_HISTORY
            MENU -> MainNavDest.MENU
        }
    }
}

@Composable
fun WaterworksBureauBottomBar(
    onItemClicked: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = BottomNavItem.values().sortedBy { it.order }
    var preSelectedIndex by remember { mutableStateOf(0) }
    var selectedIndex by remember { mutableStateOf(0) }

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

    WaterWorksBureauBottomNavLayout(
        selectedIndex = selectedIndex,
        itemCount = items.size,
        modifier = modifier,
        backgroundBack = {
            TopWavyLineBox(color = SubBlue)
        },
        backgroundFront = {
            TopWavyLineBox(color = MainBlue)
        },
        dropletIndicator = {
            AnimationDroplet(selectedIndex = selectedIndex, preSelectedIndex = preSelectedIndex)
        },

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
                    onItemClicked(item)
                }
            )
        }
    }
}

@Composable
fun WaterWorksBureauBottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    backgroundBack: @Composable BoxScope.() -> Unit,
    backgroundFront: @Composable BoxScope.() -> Unit,
    dropletIndicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val indicatorMovingAnimSpec = TweenSpec<Float>(
        durationMillis = 1000
    )
    val backgroundMovingAnimSpec = TweenSpec<Float>(
        durationMillis = 1000
    )

    val targetIndex = selectedIndex.toFloat()
    val indicatorIndex = remember { Animatable(0f) }
    LaunchedEffect(targetIndex) {
        indicatorIndex.animateTo(targetIndex, indicatorMovingAnimSpec)
    }


    val backgroundPoint = remember { Animatable(0f) }
    LaunchedEffect(targetIndex) {
        backgroundPoint.animateTo(targetIndex, backgroundMovingAnimSpec)
    }

    Layout(
        modifier = modifier
            .height(BottomNavHeight),
        content = {
            Box(Modifier.layoutId(BackgroundBackId), content = backgroundBack)
            Box(Modifier.layoutId(BackgroundFrontId), content = backgroundFront)
            Box(Modifier.layoutId(IndicatorId), content = dropletIndicator)
            content()
        }
    ) { measurables, constraints ->

        val itemWidth = constraints.maxWidth / itemCount

        val backgroundBackMeasurable = measurables.first { it.layoutId == BackgroundBackId }
        val backgroundBackPlaceable = backgroundBackMeasurable.measure(
            constraints.copy(
                minWidth = constraints.minWidth * 2,
                maxWidth = constraints.maxWidth * 2
            )
        )

        val backgroundFrontMeasurable = measurables.first { it.layoutId == BackgroundFrontId }
        val backgroundFrontPlaceable = backgroundFrontMeasurable.measure(
            constraints.copy(
                minWidth = constraints.minWidth * 2,
                maxWidth = constraints.maxWidth * 2
            )
        )

        val indicatorMeasurable = measurables.first { it.layoutId == IndicatorId }
        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = itemWidth,
                maxWidth = itemWidth
            )
        )

        val itemPlaceables = measurables
            .filterNot { it == backgroundBackMeasurable }
            .filterNot { it == backgroundFrontMeasurable }
            .filterNot { it == indicatorMeasurable }
            .map { measurable ->
                measurable.measure(constraints.copy(
                    minWidth = itemWidth,
                    maxWidth = itemWidth
                ))
            }

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            val backgroundBackLeft = (backgroundPoint.value * itemWidth)
            backgroundBackPlaceable.placeRelative(-backgroundBackLeft.toInt() - itemWidth / 3, -constraints.maxHeight / 8)
            val backgroundFrontLeft = backgroundPoint.value * itemWidth
            backgroundFrontPlaceable.placeRelative(-constraints.maxWidth + backgroundFrontLeft.toInt(), 0)
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

private const val BackgroundFrontId = "background_front_id"
private const val BackgroundBackId = "background_back_id"
private const val IndicatorId = "indicator"
private val BottomNavHeight = 60.dp