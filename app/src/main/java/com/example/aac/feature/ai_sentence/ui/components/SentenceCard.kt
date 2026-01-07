package com.example.aac.feature.ai_sentence.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun SentenceCard(
    text: String,
    isFavorite: Boolean,
    onEdit: () -> Unit,
    onFavorite: () -> Unit,
    onPlay: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, modifier = Modifier.weight(1f))

            Spacer(Modifier.width(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SvgButton(
                    resId = R.drawable.btn_edit, // ← 본인 drawable 이름으로
                    contentDescription = "편집",
                    onClick = onEdit,
                    size = 40.dp
                )

                SvgButton(
                    resId = R.drawable.btn_play, // ← 본인 drawable 이름으로
                    contentDescription = "재생",
                    onClick = onPlay,
                    size = 40.dp
                )

                SvgButton(
                    resId = if (isFavorite) R.drawable.btn_favorite_off else R.drawable.btn_favorite_off,
                    contentDescription = "즐겨찾기",
                    onClick = onFavorite,
                    size = 40.dp
                )
            }
        }
    }
}
