package com.example.aac.ui.features.usage_history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.aac.ui.theme.AacTheme

class UsageHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AacTheme {
                // 아까 만든 화면 컴포저블을 여기서 띄웁니다
                UsageHistoryScreen(
                    onBackClick = { finish() } // 뒤로가기 누르면 액티비티 종료
                )
            }
        }
    }
}