package com.example.aac.ui.features.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

// --- 디자인 상수 ---
val CustomBlue = Color(0xFF267FD6) // 배경색 (선택 시)
val TextBlack = Color.Black        // 텍스트 색상

// 데이터 모델
data class CategoryItem(
    val name: String,
    val iconRes: Int, // R.drawable.xxx
    val isSelected: Boolean
)

@Composable
fun CategoryBar(
    categories: List<CategoryItem>,
    onCategoryClick: (Int) -> Unit, // 클릭 이벤트 콜백
    modifier: Modifier = Modifier
) {
    // 메인 컨테이너: 가로 1137dp, 세로 68dp 고정
    Row(
        modifier = modifier
            .width(1137.dp)
            .height(68.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        // [수정됨] 이전 버튼 호출: R.drawable.ic_prev 사용
        NavigationBox(
            iconRes = R.drawable.btn_prev, // 준비하신 이미지 리소스 ID
            description = "이전",
            onClick = { /* 이전 로직 */ }
        )

        // [카테고리 탭 리스트]
        categories.forEachIndexed { index, item ->
            CategoryTabItem(
                item = item,
                onClick = { onCategoryClick(index) },
                modifier = Modifier.weight(1f)
            )

            // 구분선 표시 로직
            val isNextSelected = (index + 1 < categories.size) && categories[index + 1].isSelected
            if (index < categories.lastIndex && !item.isSelected && !isNextSelected) {
                VerticalDivider(
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
        }

        // [수정됨] 다음 버튼 호출: R.drawable.ic_next 사용 (이름 확인 필요!)
        NavigationBox(
            iconRes = R.drawable.btn_next, // "다음" 버튼 이미지 리소스 ID
            description = "다음",
            onClick = { /* 다음 로직 */ }
        )
    }
}

@Composable
private fun CategoryTabItem(
    item: CategoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (item.isSelected) CustomBlue else Color.White
    val textColor = if(item.isSelected) Color.White else TextBlack

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 원본 이미지 색상 그대로 출력
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.name,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = textColor
        )
    }
}

// NavigationBox 정의 변경
@Composable
private fun NavigationBox(
    iconRes: Int, // 매개변수 타입 변경: ImageVector -> Int
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(68.dp)
            .fillMaxHeight()
            .background(Color(0xFF66B3FF))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Icon 컴포넌트 사용법 변경: imageVector -> painter
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = description,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}