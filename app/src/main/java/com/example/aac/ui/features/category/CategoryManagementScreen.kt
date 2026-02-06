package com.example.aac.ui.features.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.aac.ui.components.CustomTopBar
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
        CustomTopBar(
            title = "카테고리 / 낱말 카드 추가",
            onBackClick = onBackClick,
            actionText = "저장하기",
            onActionClick = {
            }
        )

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
