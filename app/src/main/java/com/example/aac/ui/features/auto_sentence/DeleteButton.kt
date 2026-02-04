package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

@Composable
fun DeleteButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = Color(0xFFE7EAED),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(
                start = 21.dp,
                end = 21.dp,
                top = 9.dp,
                bottom = 9.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_trash2),
                contentDescription = "삭제",
                modifier = Modifier.size(30.dp),
                tint = Color(0xFFCC3333)
            )

            Text(
                text = "삭제하기",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFCC3333)
            )
        }
    }
}
