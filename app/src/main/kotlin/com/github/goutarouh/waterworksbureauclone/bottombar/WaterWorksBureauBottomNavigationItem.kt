package com.github.goutarouh.waterworksbureauclone.bottombar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WaterWorksBureauBottomNavigationIem(
    icon: @Composable ColumnScope.() -> Unit,
    text: @Composable ColumnScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable { if (!selected) onSelected() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        text()
    }
}