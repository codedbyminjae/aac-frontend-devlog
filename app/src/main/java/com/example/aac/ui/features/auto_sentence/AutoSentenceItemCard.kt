package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

@Composable
fun AutoSentenceItemCard(
    item: AutoSentenceItem,
    onSoundClick: (AutoSentenceItem) -> Unit = {},
    onItemClick: () -> Unit = {} // 🔥 확장 포인트
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = if (isPressed) {
        Color(0xFFF2F2F2)
    } else {
        Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 97.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // ✅ ripple 제거 (원하면 살릴 수 있음)
                onClick = onItemClick
            )
            .padding(
                start = 19.dp,
                end = 19.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        /* ---------- 텍스트 영역 ---------- */
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = item.sentence,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2C2C2C)
            )

            Spacer(modifier = Modifier.height(6.dp))

            val repeatText = item.repeatSetting.toKoreanText()
            val amPm = if (item.timeState.isAm) "오전" else "오후"
            val minuteText = item.timeState.minute.toString().padStart(2, '0')

            Text(
                text = "$repeatText / $amPm ${item.timeState.hour}:$minuteText",
                fontSize = 20.sp,
                color = Color(0xFF494949)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        /* ---------- 재생 버튼 ---------- */
        SoundButton(
            onClick = { onSoundClick(item) }
        )
    }
}


@Composable
fun SoundButton(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(58.dp)
            .background(
                color = Color(0xFF3199FF),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_sound),
            contentDescription = "재생",
            tint = Color.White,
            modifier = Modifier.size(21.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "재생",
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}



