package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AutoSentenceOptionCard(
    modifier: Modifier = Modifier,   // modifier 받기
    iconRes: Int,
    title: String,
    value: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 71.dp)   // 핵심
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(
                start = 19.dp,
                end = 19.dp,
                top = 17.dp,
                bottom = 17.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        /* 좌측 */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        /* 우측 값 */
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color(0xFF757575)
        )
    }
}
