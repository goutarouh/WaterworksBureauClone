package com.github.goutarouh.waterworksbureauclone.bottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goutarouh.waterworksbureauclone.ui.theme.WaterworksBureauCloneTheme
import kotlin.math.abs

@Composable
fun AnimationDroplet(
    selectedIndex: Int,
    preSelectedIndex: Int
) {

    val indicatorAngleAnimSpec = TweenSpec<Float>(durationMillis = 500)
    val dropLetAngle = remember { Animatable(0f) }
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

    Droplet(angle = dropLetAngle.value, alpha = dropLetAlpha.value)
}

@Composable
fun Droplet(
    angle: Float,
    alpha: Float,
    modifier: Modifier = Modifier
) {

    val dropletShape = GenericShape { size, _ ->
        val shapeRect = Rect(Offset(0f, 0f), size)
        val controlPointY = shapeRect.top + (shapeRect.bottom * 0.3f)
        arcTo(
            rect = Rect(
                offset = Offset(0f, 0f),
                size = size
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        quadraticBezierTo(
            x1 = shapeRect.centerLeft.x,
            y1 = controlPointY,
            x2 = shapeRect.topCenter.x,
            y2 = shapeRect.topCenter.y
        )
        quadraticBezierTo(
            x1 = shapeRect.centerRight.x,
            y1 = controlPointY,
            x2 = shapeRect.centerRight.x,
            y2 = shapeRect.centerRight.y
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(rotationZ = angle, alpha = alpha)
            .clip(dropletShape)
            .background(MaterialTheme.colors.background)
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF2073B2,
    widthDp = 100,
    heightDp = 100
)
@Composable
fun PreviewDroplet() {
    WaterworksBureauCloneTheme {
        Droplet(
            angle = 0f,
            alpha = 1f,
            modifier = Modifier.size(width = 100.dp, height = 100.dp)
        )
    }
}