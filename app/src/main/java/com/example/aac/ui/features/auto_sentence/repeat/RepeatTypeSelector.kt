package com.example.aac.ui.features.auto_sentence.repeat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RepeatTypeWheelSelector(
    selectedType: RepeatType,
    onTypeSelected: (RepeatType) -> Unit
) {
    val items = listOf(
        RepeatType.DAILY to "매일",
        RepeatType.WEEKLY to "매주",
        RepeatType.BIWEEKLY to "격주",
        RepeatType.MONTHLY to "매월"
    )

    val selectedIndex = items.indexOfFirst { it.first == selectedType }

    Column(
        modifier = Modifier
            .width(119.dp)
            .height(189.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items.forEachIndexed { index, item ->
            if (index in (selectedIndex - 1)..(selectedIndex + 1)) {

                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp) // ✅ 클릭 영역 추천값
                        .clickable { onTypeSelected(item.first) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.second,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected)
                            Color.Black
                        else
                            Color(0xFFD6D6D6)
                    )
                }
            }
        }
    }
}
