package com.example.aac.ui.features.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

// --- 상수 및 데이터 모델 ---
val ControlBlue = Color(0xFF64B5F6)

data class CardData(
    val text: String,
    val bgColor: Color
)

@Composable
fun CardsArea(
    cards: List<CardData>, // 외부에서 현재 페이지의 카드 데이터만 받음
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // 7열 고정
        modifier = modifier.fillMaxSize(),
        // 상하좌우 패딩 및 아이템 간격 20dp 통일
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        userScrollEnabled = false // 스크롤 비활성화 (버튼으로 페이지 이동)
    ) {
        items(cards) { card ->
            CardItemView(card = card)
        }
    }
}

@Composable
fun CardItemView(card: CardData) {
    Column(
        modifier = Modifier
            .aspectRatio(1f) // 1:1
            .clip(RoundedCornerShape(8.dp))
            .background(card.bgColor)
            .clickable { /* 카드 클릭 로직 */ }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 이미지 등 내용
        // Image(...)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = card.text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CardControlBar(
    onUpClick: () -> Unit,   //위로 버튼 클릭 콜백
    onDownClick: () -> Unit, // 아래로 버튼 클릭 콜백
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(70.dp)
            .fillMaxHeight()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // [위로 버튼] (이전 페이지)
        ControlBox(
            iconRes = R.drawable.btn_up,
            text = "위로",
            onClick = onUpClick,
            modifier = Modifier.weight(1f)
        )

        // [아래로 버튼] (다음 페이지)
        ControlBox(
            iconRes = R.drawable.btn_down,
            text = "아래로",
            onClick = onDownClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ControlBox(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ControlBlue)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}