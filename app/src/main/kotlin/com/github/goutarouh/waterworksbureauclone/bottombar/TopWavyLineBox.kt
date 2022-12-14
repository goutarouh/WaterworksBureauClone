package com.github.goutarouh.waterworksbureauclone.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goutarouh.waterworksbureauclone.ui.theme.MainBlue
import com.github.goutarouh.waterworksbureauclone.ui.theme.WaterworksBureauCloneTheme


@Composable
fun TopWavyLineBox(
    color: Color,
    modifier: Modifier = Modifier,
) {


    val topWavyLineBoxShape = GenericShape { size, _ ->

        val waveLength = size.width / 4f
        val halfPeriod = waveLength / 2f
        val amplitude = size.height / 4
        val center = amplitude / 2
        moveTo(0f, center)
        repeat( 16) {
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
            .fillMaxSize()
            .height(TOP_WAVY_LINE_BOX_HEIGHT)
            .clip(topWavyLineBoxShape)
            .background(color = color)
    )
}


private val TOP_WAVY_LINE_BOX_HEIGHT = 70.dp


@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 60
)
@Composable
fun PreviewTopWavyLineBox() {
    WaterworksBureauCloneTheme {
        TopWavyLineBox(color = MainBlue, modifier = Modifier.fillMaxWidth())
    }
}