package com.example.aac.ui.features.auto_sentence.time

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String = "완료",
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = if (enabled) {
        Color(0xFF0088FF)
    } else {
        Color(0xFFB0D4FF) // 비활성 대비용 (추후 수정 가능)
    }

    Box(
        modifier = Modifier
            .width(1092.dp)   // width
            .height(60.dp)    // height
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                start = 106.dp,
                end = 106.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,          // 24px
            fontWeight = FontWeight.Medium,
            color = Color.White        // #FFFFFF
        )
    }
}
