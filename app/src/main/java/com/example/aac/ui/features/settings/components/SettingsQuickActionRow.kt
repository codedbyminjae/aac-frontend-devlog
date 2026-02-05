package com.example.aac.ui.features.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun SettingsQuickActionRow(
    onCategoryClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onAutoSentenceClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingsQuickActionCard(
            title = "카테고리 / 낱말 카드 추가",
            backgroundColor = Color(0xFFEBF3FF),
            clickedColor = Color(0xFFD1E3FF),
            iconRes = R.drawable.ic_add,
            onClick = onCategoryClick,
            modifier = Modifier.weight(1f)
        )

        SettingsQuickActionCard(
            title = "목소리 설정",
            backgroundColor = Color(0xFFFFFBEB),
            clickedColor = Color(0xFFFFF6D1),
            iconRes = R.drawable.ic_sound2,
            onClick = onVoiceClick,
            modifier = Modifier.weight(1f)
        )

        SettingsQuickActionCard(
            title = "자동 출력 문장 설정",
            backgroundColor = Color(0xFFEFFAF1),
            clickedColor = Color(0xFFDCF4E0),
            iconRes = R.drawable.ic_calendar,
            onClick = onAutoSentenceClick,   // 그대로 사용
            modifier = Modifier.weight(1f)
        )
    }
}


@Preview(showBackground = true, widthDp = 1280)
@Composable
fun SettingsQuickActionRowPreview() {
    SettingsQuickActionRow(
        onCategoryClick = {},
        onVoiceClick = {},
        onAutoSentenceClick = {}
    )
}
