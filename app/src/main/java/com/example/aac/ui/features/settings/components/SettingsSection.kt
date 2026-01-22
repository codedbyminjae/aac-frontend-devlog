package com.example.aac.ui.features.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C2C2C),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}
