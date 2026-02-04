package com.example.aac.ui.features.voice_setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun Circle(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .then(
                if (selected) {
                    Modifier.background(Color(0xFF0088FF), CircleShape)
                } else {
                    Modifier.border(1.dp, Color(0xFFD9D9D9), CircleShape)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "checked",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
