package com.example.aac.ui.features.voice_setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VoiceOptionCard(
    title: String,
    selected: Boolean,
    onCardClick: () -> Unit,
    onPreviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoiceOptionCardFrame(
        modifier = modifier.clickable(onClick = onCardClick)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Circle(selected = selected)

                Spacer(modifier = Modifier.width(12.dp)) // 원-텍스트 간격(나중에 피그마값으로 조정)

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C2C2C)
                )
            }

            SoundButton(onClick = onPreviewClick)
        }
    }
}
