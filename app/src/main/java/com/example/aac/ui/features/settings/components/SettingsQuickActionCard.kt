package com.example.aac.ui.features.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

@Composable
fun SettingsQuickActionCard(
    title: String,
    backgroundColor: Color,
    clickedColor: Color,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 클릭 상태 관리
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFD9D9D9)),
        colors = CardDefaults.cardColors(
            containerColor = if (isPressed) clickedColor else backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 26.dp, end = 26.dp, top = 24.dp, bottom = 24.dp)
        ) {
            // 좌상단 텍스트
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C2C2C),
                modifier = Modifier.align(Alignment.TopStart)
            )

            // 우하단 아이콘
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SettingsQuickActionCardPreview() {
    SettingsQuickActionCard(
        title = "카테고리 / 낱말 카드 추가",
        backgroundColor = Color(0xFFEBF3FF),
        clickedColor = Color(0xFFD1E3FF),
        iconRes = R.drawable.ic_add,
        onClick = {}
    )
}
