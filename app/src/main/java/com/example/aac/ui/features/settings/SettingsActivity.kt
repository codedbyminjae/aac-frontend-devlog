package com.example.aac.ui.features.settings

import android.content.Intent // Intent 임포트 필요
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.aac.ui.features.usage_history.UsageHistoryActivity // 새로 만들 액티비티 임포트
import com.example.aac.ui.theme.AacTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AacTheme {
                SettingsScreen(
                    onBackClick = { finish() },
                    onAutoSentenceSettingClick = {
                        // Activity에서는 아직 안 씀
                    },
                    onVoiceSettingClick = {
                        // Activity에서는 아직 안 씀
                    },

                    onUsageHistoryClick = {
                        val intent = Intent(this, UsageHistoryActivity::class.java)
                        startActivity(intent)
                    },

                    onCategoryManagementClick = {},

                    onSpeakSettingClick = {}
                )
            }
        }
    }
}