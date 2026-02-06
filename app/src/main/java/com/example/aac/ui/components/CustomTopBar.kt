package com.example.aac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTopBar(
    title: String,
    onBackClick: () -> Unit,

    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    actionColor: Color = Color(0xFF1C63A8),

    actions: (@Composable () -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(123.dp)
                .background(Color(0xFFF0F0F0))
                .padding(horizontal = 88.dp),
            verticalAlignment = Alignment.Top
        ) {

             Row(
                modifier = Modifier
                    .padding(top = 62.dp)
                    .width(120.dp)
                    .height(45.dp)
                    .clickable(onClick = onBackClick)
                    .padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF66B3FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("뒤로가기", color = Color(0xFF373737), fontSize = 15.sp)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(top = 62.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 72.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                if (actions != null) {
                    actions()
                } else if (actionText != null && onActionClick != null) {
                    Box(
                        modifier = Modifier
                            .width(63.dp)
                            .height(21.dp)
                            .clickable(onClick = onActionClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = actionText,
                            color = actionColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 21.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(63.dp))
                }
            }
        }

        HorizontalDivider(color = Color(0xFFDCDCDC), thickness = 2.dp)
    }
}