package com.example.aac.ui.features.voice_setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.components.CommonSaveDialog

@Composable
fun VoiceSettingScreen(
    initialSelectedId: String,
    onBackClick: () -> Unit = {},
    onSave: (String) -> Unit = {}
) {
    val options = remember {
        listOf(
            VoiceOption("boy", "남자 아이 목소리"),
            VoiceOption("girl", "여자 아이 목소리"),
            VoiceOption("default_male", "기본 남성 목소리"),
            VoiceOption("default_female", "기본 여성 목소리"),
            VoiceOption("grandpa", "할아버지 목소리"),
            VoiceOption("grandma", "할머니 목소리"),
        )
    }

    var selectedId by remember(initialSelectedId) { mutableStateOf(initialSelectedId) }
    val hasChanges = selectedId != initialSelectedId

    var showSaveDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (hasChanges) showSaveDialog = true
        else onBackClick()
    }

    Scaffold(
        containerColor = Color(0xFFF4F4F4),
        topBar = {
            CustomTopBar(
                title = "목소리 설정",
                onBackClick = {
                    if (hasChanges) showSaveDialog = true
                    else onBackClick()
                },

                actionText = "저장하기",
                onActionClick = {
                    onSave(selectedId)
                    onBackClick()
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 25.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(options, key = { it.id }) { option ->
                VoiceOptionCard(
                    title = option.title,
                    selected = option.id == selectedId,
                    onCardClick = { selectedId = option.id },
                    onPreviewClick = { /* TODO */ }
                )
            }
        }
    }

    if (showSaveDialog) {
        CommonSaveDialog(
            message = "변경사항을\n저장하시겠어요?",
            onDismiss = { showSaveDialog = false },
            onSave = {
                showSaveDialog = false
                onSave(selectedId)
                onBackClick()
            }
        )
    }
}