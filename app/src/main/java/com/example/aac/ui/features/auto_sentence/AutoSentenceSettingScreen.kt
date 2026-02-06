package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.components.CommonDeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoSentenceSettingScreen(
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (AutoSentenceItem) -> Unit,
    onSelectDeleteClick: () -> Unit,
    onDeleteAll: () -> Unit,
    autoSentenceList: List<AutoSentenceItem>
) {
    var showMoreMenu by rememberSaveable { mutableStateOf(false) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            CustomTopBar(
                title = "자동 출력 문장 설정",
                onBackClick = onBack,

                actionText = "더보기",
                actionColor = Color.Black,
                onActionClick = {
                    showMoreMenu = !showMoreMenu
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                AutoSentenceAddButton(onClick = onAddClick)

                Spacer(modifier = Modifier.height(24.dp))

                if (autoSentenceList.isEmpty()) {
                    Text(
                        text = "등록된 문장이 없습니다.",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                } else {
                    autoSentenceList.forEach { item ->
                        AutoSentenceItemCard(
                            item = item,
                            onItemClick = { onEditClick(item) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            if (showMoreMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showMoreMenu = false }
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 24.dp)
                        .width(137.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            Color(0xFFD9D9D9),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    MoreMenuItem("선택 삭제") {
                        showMoreMenu = false
                        onSelectDeleteClick()
                    }

                    MoreMenuItem("전체 삭제") {
                        showMoreMenu = false
                        showDeleteAllDialog = true
                    }
                }
            }
        }
    }

    if (showDeleteAllDialog) {
        CommonDeleteDialog(
            message = "자동 출력 문장을\n모두 삭제 하시겠어요?",
            onDismiss = {
                showDeleteAllDialog = false
            },
            onDelete = {
                onDeleteAll()
                showDeleteAllDialog = false
            }
        )
    }
}

@Composable
fun MoreMenuItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(53.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}