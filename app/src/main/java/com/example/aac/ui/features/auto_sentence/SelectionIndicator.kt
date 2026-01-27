package com.example.aac.ui.features.auto_sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun SelectionIndicator(
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .then(
                if (isSelected) {
                    Modifier.background(
                        color = Color(0xFF4C82F7), // 선택 시 파란 원
                        shape = CircleShape
                    )
                } else {
                    Modifier.border(
                        width = 1.5.dp,
                        color = Color(0xFFD9D9D9), // 미선택 회색 테두리
                        shape = CircleShape
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "선택됨",
                tint = Color.White,          // 🔥 흰색 체크
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


@Composable
fun SelectableAutoSentenceItem(
    item: AutoSentenceItem,
    isSelected: Boolean,
    onToggleSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ⭕ / ✔ 선택 인디케이터
        SelectionIndicator(isSelected = isSelected)

        Spacer(modifier = Modifier.width(12.dp))

        // 기존 카드 재사용 (편집 이동 막음)
        AutoSentenceItemCard(
            item = item,
            onItemClick = {}
        )
    }
}
