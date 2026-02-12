package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.components.CommonDeleteDialog

@Composable
fun AutoSentenceSelectDeleteScreen(
    autoSentenceList: List<AutoSentenceItem>,
    onBack: () -> Unit,
    onDeleteSelected: (Set<Long>) -> Unit
) {
    var selectedIds by remember { mutableStateOf(setOf<Long>()) }
    var showDeleteDialog by remember { mutableStateOf(false) } // 모달 상태

    // 삭제 버튼 색상 계산
    val deleteButtonColor = if (selectedIds.isNotEmpty()) {
        Color(0xFFE53935)
    } else {
        Color(0xFFB0B0B0)
    }

    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            CustomTopBar(
                title = "자동 출력 문장 설정",
                onBackClick = onBack,

                actionText = "삭제하기",
                actionColor = deleteButtonColor,
                onActionClick = {
                    if (selectedIds.isNotEmpty()) {
                        showDeleteDialog = true
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            autoSentenceList.forEach { item ->
                SelectableAutoSentenceItem(
                    item = item,
                    isSelected = selectedIds.contains(item.id),
                    onToggleSelect = {
                        selectedIds =
                            if (selectedIds.contains(item.id))
                                selectedIds - item.id
                            else
                                selectedIds + item.id
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    /* ---------- 공통 삭제 확인 모달 적용 ---------- */
    if (showDeleteDialog) {
        CommonDeleteDialog(
            message = "선택한 문장을\n삭제 하시겠어요?",
            onDismiss = {
                showDeleteDialog = false
            },
            onDelete = {
                onDeleteSelected(selectedIds)
                showDeleteDialog = false
            }
        )
    }
}