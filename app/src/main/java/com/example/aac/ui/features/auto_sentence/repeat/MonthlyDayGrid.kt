package com.example.aac.ui.features.auto_sentence.repeat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MonthlyDayGrid(
    selectedDays: Set<Int>,
    onDayToggle: (Int) -> Unit
) {
    // 1 ~ 31
    val days = (1..31).toList()

    // 7개씩 끊어서 Row로 사용
    val rows = days.chunked(7)

    Column(
        modifier = Modifier
            .width(273.dp)
            .height(195.dp)
    ) {
        rows.forEach { week ->
            Row(
                modifier = Modifier
                    .width(273.dp)
                    .height(39.dp)
            ) {
                week.forEach { day ->
                    MonthlyDayCell(
                        day = day,
                        selected = selectedDays.contains(day),
                        onClick = {
                            onDayToggle(day)
                        }
                    )
                }
            }
        }
    }
}
