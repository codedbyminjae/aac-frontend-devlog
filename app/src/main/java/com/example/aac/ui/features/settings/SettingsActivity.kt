package com.example.aac.ui.features.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.aac.ui.theme.AacTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AacTheme {
                SettingsScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}
