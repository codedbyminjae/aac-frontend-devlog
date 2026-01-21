package com.example.aac.ui.features.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.ui.features.category.components.ManagementTabRow

@Composable
fun CategoryManagementScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        CustomTopBar(onBackClick = onBackClick)

        ManagementTabRow(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        if (selectedTabIndex == 0) {
            CategoryManagementContent()
        } else {
            WordCardManagementContent()
        }
    }
}

@Composable
fun CustomTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(123.dp)
            .background(Color(0xFFF4F4F4))
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // 뒤로가기 버튼
        Row(
            modifier = Modifier.clickable(onClick = onBackClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
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
            Text("뒤로가기", color = Color.Black, fontSize = 18.sp)
        }

        // 중앙 타이틀
        Text(
            text = "카테고리 / 낱말 카드 추가",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // 저장하기 버튼
        TextButton(
            onClick = { },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                "저장하기",
                color = Color(0xFF1C63A8),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }

    HorizontalDivider(
        color = Color(0xFFDCDCDC),
        thickness = 1.dp
    )
}