package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AutoSentenceSelectDeleteScreen(
    autoSentenceList: List<AutoSentenceItem>,
    onBack: () -> Unit,
    onDeleteSelected: (Set<Long>) -> Unit
) {
    var selectedIds by remember { mutableStateOf(setOf<Long>()) }
    var showDeleteDialog by remember { mutableStateOf(false) } // ⭐ 모달 상태

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "자동 출력 문장 설정",
                rightText = "삭제하기",
                rightTextColor =
                    if (selectedIds.isNotEmpty()) Color(0xFFE53935)
                    else Color(0xFFB0B0B0),
                onBackClick = onBack,
                onRightClick = {
                    if (selectedIds.isNotEmpty()) {
                        showDeleteDialog = true   // ⭐ 바로 삭제 ❌ → 모달 표시
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

    /* ---------- 선택 삭제 확인 모달 ---------- */
    if (showDeleteDialog) {
        AutoSentenceDeleteConfirmDialog(
            message = "선택한 문장을\n\n삭제 하시겠어요?",
            onCancel = {
                showDeleteDialog = false
            },
            onConfirm = {
                onDeleteSelected(selectedIds) // 실제 삭제
                showDeleteDialog = false
            }
        )
    }
}
