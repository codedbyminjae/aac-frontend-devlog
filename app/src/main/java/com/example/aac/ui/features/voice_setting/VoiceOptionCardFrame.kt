package com.example.aac.ui.features.voice_setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VoiceOptionCardFrame(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .width(1118.dp)
            .height(78.dp)
            .background(Color(0xFFFFFFFF), shape)
            .border(1.dp, Color(0xFFD9D9D9), shape)
            .padding(
                start = 19.dp,
                end = 19.dp,
                top = 10.dp,
                bottom = 10.dp
            )
    ) {
        content()
    }
}
