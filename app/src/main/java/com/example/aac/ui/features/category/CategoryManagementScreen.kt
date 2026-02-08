package com.example.aac.ui.features.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.aac.R
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.components.CommonSaveDialog
import com.example.aac.ui.features.category.components.ManagementTabRow

@Composable
fun CategoryManagementScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showSaveDialog by remember { mutableStateOf(false) }

    val categoryList = remember {
        mutableStateListOf(
            CategoryEditData(iconRes = R.drawable.ic_default, title = "기본", count = 5),
            CategoryEditData(iconRes = R.drawable.ic_human, title = "사람", count = 13),
            CategoryEditData(iconRes = R.drawable.ic_act, title = "행동", count = 13),
            CategoryEditData(iconRes = R.drawable.ic_place, title = "장소", count = 8),
            CategoryEditData(iconRes = R.drawable.ic_emotion, title = "감정", count = 12),
            CategoryEditData(iconRes = R.drawable.ic_food, title = "음식", count = 20)
        )
    }

    val wordList = remember {
        mutableStateListOf(
            WordCardData("ADD_BUTTON", "낱말 추가", Color.White),
            WordCardData("1", "먹다", Color(0xFFC8E6C9)),
            WordCardData("2", "가다", Color(0xFFC8E6C9)),
            WordCardData("3", "크다", Color(0xFFBBDEFB)),
            WordCardData("4", "학교", Color(0xFFFFE0B2)),
            WordCardData("5", "위", Color(0xFFE1BEE7)),
            WordCardData("6", "아래", Color(0xFFE1BEE7)),
            WordCardData("7", "나", Color(0xFFFFF9C4)),
            WordCardData("8", "너", Color(0xFFFFF9C4))
        )
    }

    val initialCategoryList = remember { categoryList.toList() }
    val initialWordList = remember { wordList.toList() }

    val hasChanges by remember {
        derivedStateOf {
            categoryList.toList() != initialCategoryList || wordList.toList() != initialWordList
        }
    }

    BackHandler {
        if (hasChanges) {
            showSaveDialog = true
        } else {
            onBackClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        CustomTopBar(
            title = "카테고리 / 낱말 카드 추가",

            onBackClick = {
                if (hasChanges) {
                    showSaveDialog = true
                } else {
                    onBackClick()
                }
            },

            actionText = "저장하기",
            onActionClick = {
                onBackClick()
            }
        )

        ManagementTabRow(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        if (selectedTabIndex == 0) {
            CategoryManagementContent(categoryList = categoryList)
        } else {
            WordCardManagementContent(wordList = wordList)
        }
    }

    if (showSaveDialog) {
        CommonSaveDialog(
            message = "변경사항을\n저장하시겠어요?",
            onDismiss = { showSaveDialog = false },
            onSave = {
                showSaveDialog = false
                onBackClick()
            }
        )
    }
}