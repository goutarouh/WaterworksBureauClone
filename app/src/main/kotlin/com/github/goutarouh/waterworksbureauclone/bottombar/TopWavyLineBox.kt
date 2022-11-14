package com.github.goutarouh.waterworksbureauclone.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goutarouh.waterworksbureauclone.ui.theme.WaterworksBureauCloneTheme


@Composable
fun TopWavyLineBox(
    modifier: Modifier,
) {

    val density = LocalDensity.current

    val topWavyLineBoxShape = GenericShape { size, _ ->

        val waveLength = size.width / 2f
        val halfPeriod = waveLength / 2f
        val amplitude = with(density) { 20.dp.toPx() }
        val center = amplitude / 2
        moveTo(0f, center)
        repeat(8) {
            quadraticBezierTo(
                x1 = (halfPeriod / 2) * (2 * it + 1),
                y1 = center + (center * if (it % 2 == 0) 1 else -1),
                x2 = halfPeriod * (it + 1), y2 = center
            )
        }
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
    }

    Box(
        modifier = modifier
            .height(TOP_WAVY_LINE_BOX_HEIGHT)
            .clip(topWavyLineBoxShape)
            .background(MaterialTheme.colors.primary)
    )
}


private val TOP_WAVY_LINE_BOX_HEIGHT = 70.dp


@Preview(
    showBackground = true,
    widthDp = 300
)
@Composable
fun PreviewTopWavyLineBox() {
    WaterworksBureauCloneTheme {
        TopWavyLineBox(modifier = Modifier.fillMaxWidth())
    }
}