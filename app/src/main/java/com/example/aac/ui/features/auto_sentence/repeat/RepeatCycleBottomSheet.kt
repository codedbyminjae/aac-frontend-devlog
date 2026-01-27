package com.example.aac.ui.features.auto_sentence.repeat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatCycleBottomSheet(
    onDismiss: () -> Unit,
    onComplete: (RepeatSetting) -> Unit

) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // 반복 타입 (기본: 매일)
    var selectedType by remember {
        mutableStateOf(RepeatType.DAILY)
    }

    // 요일 선택 상태
    var selectedDays by remember {
        mutableStateOf(setOf<Weekday>())
    }

    // 매월 날짜 선택 상태
    var selectedMonthDays by remember {
        mutableStateOf(setOf<Int>())
    }

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .shadow(
                    elevation = 13.3.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                /* ---------- 타이틀 ---------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "이 문장을 언제 재생할까요?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "닫기",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                /* ---------- 반복 타입 + 요일 선택 (중앙 정렬) ---------- */
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // 반복 타입 선택
                        RepeatTypeWheelSelector(
                            selectedType = selectedType,
                            onTypeSelected = { selectedType = it }
                        )

                        // 요일 선택 (매주 / 격주만)
                        // 오른쪽 영역 (타입별)
                        when (selectedType) {

                            RepeatType.WEEKLY,
                            RepeatType.BIWEEKLY -> {
                                WeekdayRow(
                                    selectedDays = selectedDays,
                                    onDayToggle = { day ->
                                        selectedDays =
                                            if (selectedDays.contains(day))
                                                selectedDays - day
                                            else
                                                selectedDays + day
                                    }
                                )
                            }

                            RepeatType.MONTHLY -> {
                                MonthlyDayGrid(
                                    selectedDays = selectedMonthDays,
                                    onDayToggle = { day ->
                                        selectedMonthDays =
                                            if (selectedMonthDays.contains(day))
                                                selectedMonthDays - day
                                            else
                                                selectedMonthDays + day
                                    }
                                )
                            }

                            RepeatType.DAILY -> {
                                // 레이아웃 유지용 빈 공간
                                Spacer(modifier = Modifier.width(273.dp))
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                /* ---------- 완료 버튼 ---------- */
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PrimaryCompleteButton(
                        onClick = {
                            onComplete(
                                RepeatSetting(
                                    type = selectedType,
                                    days = selectedDays,
                                    monthDays = selectedMonthDays
                                )
                            )
                        }
                    )

                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
