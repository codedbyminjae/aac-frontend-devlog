package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

@Composable
fun AutoSentenceAddButton(
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = if (isPressed) {
        Color(0xFF0080FF) // pressed
    } else {
        Color(0xFF3199FF) // default
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = Color(0xFF3199FF),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // ripple 없음
                onClick = onClick
            )
            .padding(
                start = 19.dp,
                end = 19.dp,
                top = 16.dp,
                bottom = 16.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 아이콘 (36px)
            Image(
                painter = painterResource(id = R.drawable.ic_add2),
                contentDescription = "문장 추가",
                modifier = Modifier.size(36.dp)
            )

            // 텍스트 (28px)
            Text(
                text = "문장 추가",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
