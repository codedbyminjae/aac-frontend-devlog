package com.example.aac.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.net.URLEncoder

fun getBackgroundColorByPartOfSpeech(partOfSpeech: String): Color {
    return when (partOfSpeech) {
        "NOUN" -> Color(0xFFFFE099)
        "VERB" -> Color(0xFFC2ECC9)
        "ADJECTIVE" -> Color(0xFFCCE0FF)
        "ADVERB", "PREPOSITION", "MODIFIER" -> Color(0xFFF0C2FF)
        else -> Color(0xFFFBFBF8)
    }
}

fun getSafeUrl(url: String): String {
    return try {
        if (url.isBlank()) return url

        val fileName = url.substringAfterLast("/")
        val baseUrl = url.substringBeforeLast("/")

        val encodedName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")

        "$baseUrl/$encodedName"
    } catch (e: Exception) {
        url
    }
}

@Composable
fun WordCard(
    text: String,
    imageUrl: String,
    partOfSpeech: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    onClick: () -> Unit = {}
) {
    val backgroundColor = getBackgroundColorByPartOfSpeech(partOfSpeech)

    val safeImageUrl = remember(imageUrl) { getSafeUrl(imageUrl) }

    Log.e("IMAGE_TEST", "변환된 URL: $safeImageUrl")

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(safeImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = text,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}