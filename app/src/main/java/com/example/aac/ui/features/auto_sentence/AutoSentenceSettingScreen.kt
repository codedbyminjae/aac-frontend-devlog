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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoSentenceSettingScreen(
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (AutoSentenceItem) -> Unit,
    onSelectDeleteClick: () -> Unit,
    onDeleteAll: () -> Unit,                 // ⭐ 전체 삭제 콜백 추가
    autoSentenceList: List<AutoSentenceItem>
) {
    var showMoreMenu by rememberSaveable { mutableStateOf(false) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            CommonTopBar(
                title = "자동 출력 문장 설정",
                rightText = "더보기",
                onBackClick = onBack,
                onRightClick = { showMoreMenu = !showMoreMenu }
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

                // ➕ 문장 추가
                AutoSentenceAddButton(onClick = onAddClick)

                Spacer(modifier = Modifier.height(24.dp))

                // 📋 자동 출력 문장 리스트
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

            /* ---------- 더보기 메뉴 ---------- */
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

    /* ---------- 전체 삭제 확인 모달 ---------- */
    if (showDeleteAllDialog) {
        AutoSentenceDeleteConfirmDialog(
            message = "자동 출력 문장을\n\n모두 삭제 하시겠어요?",
            onCancel = {
                showDeleteAllDialog = false
            },
            onConfirm = {
                onDeleteAll()               // ✅ 실제 전체 삭제
                showDeleteAllDialog = false
            }
        )
    }
}

/* ======================================================
   더보기 메뉴 아이템
   ====================================================== */
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
