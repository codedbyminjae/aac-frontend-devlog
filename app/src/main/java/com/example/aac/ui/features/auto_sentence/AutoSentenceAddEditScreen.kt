package com.example.aac.ui.features.auto_sentence

import AutoSentenceInputField
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aac.R
import com.example.aac.ui.features.auto_sentence.repeat.*
import com.example.aac.ui.features.auto_sentence.time.TimePickerBottomSheet
import com.example.aac.ui.features.auto_sentence.time.TimeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoSentenceAddEditScreen(
    mode: AutoSentenceMode,
    initialItem: AutoSentenceItem? = null,
    onBack: () -> Unit,
    onSave: (AutoSentenceItem) -> Unit,
    onDelete: (() -> Unit)? = null   // ⭐ DELETE 콜백 추가
) {

    /* ---------- 초기 상태 ---------- */
    var sentence by rememberSaveable {
        mutableStateOf(initialItem?.sentence ?: "")
    }

    var repeatSetting by remember {
        mutableStateOf(
            initialItem?.repeatSetting
                ?: RepeatSetting(
                    type = RepeatType.WEEKLY,
                    days = setOf(
                        Weekday.TUE,
                        Weekday.WED,
                        Weekday.THU
                    )
                )
        )
    }

    var timeState by remember {
        mutableStateOf(
            initialItem?.timeState
                ?: TimeState(
                    isAm = true,
                    hour = 9,
                    minute = 0
                )
        )
    }

    var isRepeatSelected by remember {
        mutableStateOf(initialItem != null)
    }

    var isTimeSelected by remember {
        mutableStateOf(initialItem != null)
    }

    /* ---------- BottomSheet 상태 ---------- */
    var showRepeatSheet by remember { mutableStateOf(false) }
    var showTimeSheet by remember { mutableStateOf(false) }

    /* ---------- 반복 주기 텍스트 ---------- */
    val repeatText = remember(repeatSetting, isRepeatSelected) {
        if (!isRepeatSelected) {
            "선택"
        } else {
            repeatSetting.toKoreanText()
        }
    }

    /* ---------- 시간 텍스트 ---------- */
    val timeText = remember(timeState, isTimeSelected) {
        if (!isTimeSelected) {
            "선택"
        } else {
            val amPm = if (timeState.isAm) "오전" else "오후"
            val minuteText = timeState.minute.toString().padStart(2, '0')
            "$amPm ${timeState.hour}:$minuteText"
        }
    }

    /* ---------- 타이틀 ---------- */
    val titleText = if (mode == AutoSentenceMode.ADD) {
        "문장 추가"
    } else {
        "문장 편집"
    }

    val isFormValid = sentence.isNotBlank()
            && isRepeatSelected
            && isTimeSelected

    val saveButtonColor = if (isFormValid) {
        Color(0xFF1C63A8)
    } else {
        Color(0xFFB0B0B0) // 비활성 색 (원하면 조정)
    }


    /* ---------- UI ---------- */
    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            CommonTopBar(
                title = titleText,
                rightText = "저장하기",
                rightTextColor = saveButtonColor,
                onBackClick = onBack,
                onRightClick = {
                    if (!isFormValid) return@CommonTopBar

                    val item = AutoSentenceItem(
                        sentence = sentence,
                        repeatSetting = repeatSetting,
                        timeState = timeState
                    )
                    onSave(item)
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

            AutoSentenceInputField(
                value = sentence,
                onValueChange = { sentence = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AutoSentenceOptionCard(
                    modifier = Modifier.weight(1f),   // ⭐ 동일한 폭
                    iconRes = R.drawable.ic_recycle,
                    title = "반복 주기",
                    value = repeatText,
                    onClick = { showRepeatSheet = true }
                )

                AutoSentenceOptionCard(
                    modifier = Modifier.weight(1f),   // ⭐ 동일한 폭
                    iconRes = R.drawable.ic_time,
                    title = "시간",
                    value = timeText,
                    onClick = { showTimeSheet = true }
                )
            }

            /* ---------- 삭제 버튼 (EDIT 모드에서만) ---------- */
            if (mode == AutoSentenceMode.EDIT && onDelete != null) {
                Spacer(modifier = Modifier.height(32.dp))

                DeleteButton(
                    onClick = onDelete
                )
            }
        }

        /* ---------- Repeat BottomSheet ---------- */
        if (showRepeatSheet) {
            RepeatCycleBottomSheet(
                onDismiss = { showRepeatSheet = false },
                onComplete = { newSetting ->
                    repeatSetting = newSetting
                    isRepeatSelected = true
                    showRepeatSheet = false
                }
            )
        }

        /* ---------- Time BottomSheet ---------- */
        if (showTimeSheet) {
            TimePickerBottomSheet(
                onDismiss = { showTimeSheet = false },
                onConfirm = { newTime ->
                    timeState = newTime
                    isTimeSelected = true
                    showTimeSheet = false
                }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 720)
@Composable
fun AutoSentenceAddEditScreenPreview() {
    AutoSentenceAddEditScreen(
        mode = AutoSentenceMode.EDIT,
        initialItem = null,
        onBack = {},
        onSave = {},
        onDelete = {}
    )
}
