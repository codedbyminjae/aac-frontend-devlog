package com.example.aac.ui.features.speak_setting.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.aac.R
import com.example.aac.data.model.SpeakSettingCardUiModel

@Composable
fun SpeakSettingCardItem(
    data: SpeakSettingCardUiModel,
    cardSize: Dp,
    cardRadius: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(cardSize)
            .clip(RoundedCornerShape(cardRadius))
            .background(data.backgroundColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (data.imageUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = data.text,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(cardSize * 0.5f)
                    .aspectRatio(1f)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = data.text,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(cardSize * 0.5f)
                    .aspectRatio(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = data.text,
            fontSize = if (cardSize > 200.dp) 24.sp else 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}