package com.github.goutarouh.waterworksbureauclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.github.goutarouh.waterworksbureauclone.ui.theme.WaterworksBureauCloneTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterworksBureauCloneTheme {
                WaterworksBureauApp()
            }
        }
    }
}