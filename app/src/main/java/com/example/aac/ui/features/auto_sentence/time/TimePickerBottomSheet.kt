package com.example.aac.ui.features.auto_sentence.time

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (TimeState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isAm by remember { mutableStateOf(true) }
    var selectedHour by remember { mutableStateOf(1) }
    var selectedMinute by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
                .shadow(
                    elevation = 13.dp,
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

                /* ---------- Title ---------- */
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "이 문장을 언제 재생할까요?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Text(
                        text = "✕",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                /* ---------- 오전 / 오후 ---------- */
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    AmPmButton("오전", isAm) { isAm = true }
                    AmPmButton("오후", !isAm) { isAm = false }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {

                        /* ---------- 시 ---------- */
                        SectionLabel("시")
                        TimeGrid(
                            values = (1..12).toList(),
                            selectedValue = selectedHour,
                            valueToText = { it.toString() },
                            onSelect = { selectedHour = it }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        /* ---------- 분 ---------- */
                        SectionLabel("분")
                        TimeGrid(
                            values = listOf(
                                0, 5, 10, 15, 20, 25,
                                30, 35, 40, 45, 50, 55
                            ),
                            selectedValue = selectedMinute,
                            valueToText = { it.toString().padStart(2, '0') },
                            onSelect = { selectedMinute = it }
                        )
                    }
                }


                Spacer(modifier = Modifier.weight(1f))

                /* ---------- 완료 ---------- */
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PrimaryButton(
                        onClick = {
                            onConfirm(
                                TimeState(
                                    isAm = isAm,
                                    hour = selectedHour,
                                    minute = selectedMinute
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

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 12.dp),
    )
}


@Composable
private fun AmPmButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(53.dp)
            .background(
                color = if (selected) Color(0xFF3199FF) else Color(0xFFEDEDED),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color.Black
        )
    }
}


@Composable
private fun TimeGrid(
    values: List<Int>,
    selectedValue: Int,
    valueToText: (Int) -> String,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        values.chunked(6).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                row.forEach { value ->
                    TimeGridItem(
                        text = valueToText(value),
                        selected = value == selectedValue,
                        onClick = { onSelect(value) }
                    )
                }
            }
        }
    }
}



@Composable
private fun TimeGridItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(53.dp)
            .background(
                color = if (selected) Color(0xFF3199FF) else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color.Black
        )
    }
}

