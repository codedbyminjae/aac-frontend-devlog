package com.example.aac.ui.features.terms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun AgreeCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(33.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF1C63A8),
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = if (checked) Color(0xFF1C63A8) else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Image(
                painter = painterResource(id = R.drawable.ic_check2),
                contentDescription = "checked",
                modifier = Modifier.size(24.45.dp)
            )
        }
    }
}

