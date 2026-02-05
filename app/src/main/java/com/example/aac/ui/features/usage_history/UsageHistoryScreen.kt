package com.example.aac.ui.features.usage_history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed // 👈 items -> itemsIndexed 변경
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.ui.features.auto_sentence.CommonTopBar
import com.example.aac.ui.features.usage_history.components.UsageHistoryDatePicker
import com.example.aac.ui.features.usage_history.components.UsageHistoryItem
import com.example.aac.ui.features.usage_history.components.UsageHistoryDeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageHistoryScreen(
    onBackClick: () -> Unit = {},
) {
    // 📅 날짜 상태
    var selectedYear by remember { mutableIntStateOf(2025) }
    var selectedMonth by remember { mutableIntStateOf(12) }
    var showDatePicker by remember { mutableStateOf(false) }

    // 🗑️ 선택 삭제 모드 관련 상태
    var isSelectionMode by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    // ⚠️ 삭제 모달 관련 상태
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var isDeleteAllMode by remember { mutableStateOf(false) }

    // 📋 데이터 리스트
    val historyList: SnapshotStateList<UsageRecord> = remember {
        mutableStateListOf(
            UsageRecord(1L, "어제 먹었던 반찬 그대로 먹나요?", "12/27 16:42"),
            UsageRecord(2L, "엄마 밥 주세요.", "12/27 16:42"),
            UsageRecord(3L, "배가 너무 고파요.", "12/27 16:41"),
            UsageRecord(4L, "아니요. 안 먹었어요.", "12/27 16:42"),
            UsageRecord(5L, "그건 맞죠.", "12/27 16:41"),
            UsageRecord(6L, "아니에요.", "12/27 16:42"),
            UsageRecord(7L, "모르겠는데요.", "12/27 16:42")
        )
    }

    val selectedIds: SnapshotStateList<Long> = remember { mutableStateListOf() }

    // 메뉴 텍스트 스타일
    val menuTextStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        color = Color.Black
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(123.dp)
                    .background(Color(0xFFF5F5F5))
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color(0xFFDCDCDC),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                CommonTopBar(
                    title = "사용 기록 조회",
                    rightText = if (isSelectionMode) "삭제하기" else "더보기",
                    rightTextColor = if (isSelectionMode) Color(0xFFFF4B4B) else Color.Black,
                    onBackClick = {
                        if (isSelectionMode) {
                            isSelectionMode = false
                            selectedIds.clear()
                        } else {
                            onBackClick()
                        }
                    },
                    onRightClick = {
                        if (isSelectionMode) {
                            if (selectedIds.isNotEmpty()) {
                                isDeleteAllMode = false
                                showDeleteConfirmDialog = true
                            }
                        } else {
                            showMoreMenu = true
                        }
                    }
                )

                // 드롭다운 메뉴
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 24.dp)
                        .size(1.dp)
                ) {
                    MaterialTheme(
                        shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp))
                    ) {
                        DropdownMenu(
                            expanded = showMoreMenu,
                            onDismissRequest = { showMoreMenu = false },
                            offset = DpOffset(x = 0.dp, y = 24.dp),
                            shape = RoundedCornerShape(12.dp),
                            containerColor = Color.White,
                            tonalElevation = 4.dp,
                            modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                        ) {
                            Column(
                                modifier = Modifier.width(137.dp).height(106.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().weight(1f).clickable {
                                        showMoreMenu = false
                                        isSelectionMode = true
                                    },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "선택 삭제", style = menuTextStyle)
                                }
                                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                                Box(
                                    modifier = Modifier.fillMaxWidth().weight(1f).clickable {
                                        showMoreMenu = false
                                        isDeleteAllMode = true
                                        showDeleteConfirmDialog = true
                                    },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "전체 삭제", style = menuTextStyle)
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 날짜 선택 영역
            Row(
                modifier = Modifier
                    // 👇 [수정] 1118 너비를 맞추기 위해 패딩 조정 (81.dp)
                    .padding(start = 81.dp, top = 20.dp, bottom = 10.dp)
                    .clickable { showDatePicker = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedYear}년 ${selectedMonth}월",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 28.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_toggle),
                    contentDescription = "날짜 선택",
                    tint = Color(0xFF99C1FF),
                    modifier = Modifier.size(19.dp).offset(y = 2.dp)
                )
            }

            // 리스트 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 20.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 👇 [수정] items -> itemsIndexed 로 변경하여 인덱스(index) 획득
                    itemsIndexed(historyList) { index, record ->
                        UsageHistoryItem(
                            record = record,
                            isFirstItem = (index == 0), // ✅ 첫 번째 아이템인지 확인
                            isSelectionMode = isSelectionMode,
                            isSelected = record.id in selectedIds,
                            onSelectionClick = {
                                if (record.id in selectedIds) {
                                    selectedIds.remove(record.id)
                                } else {
                                    selectedIds.add(record.id)
                                }
                            },
                            onPlayClick = { /* 재생 로직 */ }
                        )
                    }
                }
            }
        }

        // ... (모달 로직 생략 - 이전과 동일)
        if (showDatePicker) {
            UsageHistoryDatePicker(
                currentYear = selectedYear,
                currentMonth = selectedMonth,
                onDismiss = { showDatePicker = false },
                onConfirm = { year, month ->
                    selectedYear = year
                    selectedMonth = month
                    showDatePicker = false
                }
            )
        }
        if (showDeleteConfirmDialog) {
            UsageHistoryDeleteDialog(
                message = if (isDeleteAllMode) {
                    "${selectedMonth}월 사용 기록을\n모두 삭제 하시겠어요?"
                } else {
                    "선택한 사용 기록을\n삭제 하시겠어요?"
                },
                onDismiss = { showDeleteConfirmDialog = false },
                onConfirm = {
                    if (isDeleteAllMode) {
                        historyList.clear()
                    } else {
                        val toRemove = historyList.filter { it.id in selectedIds }
                        historyList.removeAll(toRemove)
                    }
                    selectedIds.clear()
                    isSelectionMode = false
                    showDeleteConfirmDialog = false
                }
            )
        }
    }
}

@Preview(
    device = "spec:width=1280dp,height=720dp,dpi=160,orientation=landscape",
    showBackground = true
)
@Composable
fun UsageHistoryScreenPreview() {
    UsageHistoryScreen()
}