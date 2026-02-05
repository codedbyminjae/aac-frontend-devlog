package com.example.aac.ui.features.speak_setting.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SpeakSettingCardData(
    val text: String,
    val iconRes: Int,
    val bgColor: Color
)

@Composable
fun SpeakSettingCardItem(
    card: SpeakSettingCardData,
    cardSize: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(cardSize)
            .clip(RoundedCornerShape(12.dp))
            .background(card.bgColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = card.iconRes),
            contentDescription = card.text,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(cardSize * 0.4f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = card.text,
            fontSize = if (cardSize > 200.dp) 24.sp else 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}