package com.example.aac.ui.features.usage_history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageHistoryDatePicker(
    currentYear: Int,
    currentMonth: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var selectedMonth by remember { mutableIntStateOf(currentMonth) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        modifier = Modifier.width(1280.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(346.dp)
        ) {
            // 1. 닫기 버튼
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.Black
                )
            }

            // 2. 메인 컨텐츠
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp)
                    // ✅ [수정 1] 전체 시작점을 왼쪽으로 당김 (160dp -> 60dp)
                    // 이 값을 줄이면 연도 컴포넌트가 왼쪽으로 이동합니다.
                    .padding(start = 60.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // [좌측] 연도 선택
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                    // ❌ 기존 padding(end = 150.dp) 제거 (Spacer로 대체)
                ) {
                    // 이전 연도
                    Text(
                        text = "${selectedYear - 1}",
                        fontSize = 24.sp,
                        color = Color(0xFFC4C4C4),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { selectedYear-- }
                    )

                    // 현재 연도
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$selectedYear",
                            style = TextStyle(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 36.sp,
                                textAlign = TextAlign.Center,
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                ),
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "년",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        )
                    }

                    // 다음 연도
                    Text(
                        text = "${selectedYear + 1}",
                        fontSize = 24.sp,
                        color = Color(0xFFC4C4C4),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { selectedYear++ }
                    )
                }

                // ✅ [수정 2] 연도와 월 사이의 간격을 벌려주는 Spacer 추가
                // 이 값을 늘리면 월 컴포넌트가 오른쪽으로 더 밀려납니다.
                Spacer(modifier = Modifier.width(150.dp))

                // [우측] 월 선택 + '월' 글자
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 월 버튼 뭉치 (Grid)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1~6월
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            (1..6).forEach { month ->
                                MonthButton(
                                    month = month,
                                    isSelected = month == selectedMonth,
                                    onClick = { selectedMonth = month }
                                )
                            }
                        }
                        // 7~12월
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            (7..12).forEach { month ->
                                MonthButton(
                                    month = month,
                                    isSelected = month == selectedMonth,
                                    onClick = { selectedMonth = month }
                                )
                            }
                        }
                    }

                    // '월' 텍스트
                    Text(
                        text = "월",
                        modifier = Modifier.padding(start = 8.dp),
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 36.sp,
                            letterSpacing = 0.sp,
                            textAlign = TextAlign.Center,
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            ),
                            color = Color.Black
                        )
                    )
                }
            }

            // 3. 완료 버튼
            Button(
                onClick = { onConfirm(selectedYear, selectedMonth) },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 260.dp)
                    .width(1092.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0088FF)
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "완료",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun MonthButton(
    month: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF3199FF) else Color(0xFFE7EAED)
    val textColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (isSelected) Color.Transparent else Color(0xFFD9D9D9)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(53.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "$month",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
                textAlign = TextAlign.Center,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                ),
                color = textColor
            )
        )
    }
}