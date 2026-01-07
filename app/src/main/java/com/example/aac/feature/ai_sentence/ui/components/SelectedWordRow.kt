package com.example.aac.feature.ai_sentence.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun SelectedWordRow(
    words: List<String>,
    onPlayTop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "선택한 낱말" SVG 버튼
        Image(
            painter = painterResource(R.drawable.btn_selected_word),
            contentDescription = "선택한 낱말",
            modifier = Modifier
                .height(44.dp)         // 피그마 높이에 맞춰 조절
                .clickable { /* 선택한 낱말 클릭 */ }
        )

        // "픽토그램" SVG 버튼 (또는 words[1] 같은 걸로 매핑 가능)
        Image(
            painter = painterResource(R.drawable.btn_pictogram),
            contentDescription = "픽토그램",
            modifier = Modifier
                .height(44.dp)
                .clickable { /* 픽토그램 클릭 */ }
        )

        // 오른쪽 "재생" SVG 버튼
        Image(
            painter = painterResource(R.drawable.btn_play),
            contentDescription = "재생",
            modifier = Modifier
                .height(44.dp)
                .clickable(onClick = onPlayTop)
        )
    }
}
