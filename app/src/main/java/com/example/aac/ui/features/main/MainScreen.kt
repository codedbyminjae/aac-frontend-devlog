package com.example.aac.ui.features.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.ui.features.main.components.CategoryBar
import com.example.aac.ui.features.main.components.CategoryItem
import com.example.aac.ui.features.main.components.CardsArea
import com.example.aac.ui.features.main.components.CardControlBar
import com.example.aac.ui.features.main.components.CardData

val SideBarGray = Color(0xFF666666)

@Composable
fun MainScreen(onNavigateToAiSentence: () -> Unit = {}) {
    // 1. 상태 관리 (현재 선택된 카테고리 인덱스)
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    // 2. 카테고리 데이터
    val categoryList = remember(selectedCategoryIndex) {
        listOf(
            CategoryItem("최근사용", R.drawable.ic_recent_use, false),
            CategoryItem("즐겨찾기", R.drawable.ic_favorite, false),
            CategoryItem("기본", R.drawable.ic_default, false),
            CategoryItem("사람", R.drawable.ic_human, false),
            CategoryItem("행동", R.drawable.ic_act, false),
            CategoryItem("감정", R.drawable.ic_emotion, false),
            CategoryItem("음식", R.drawable.ic_food, false),
            CategoryItem("장소", R.drawable.ic_place, false)
        ).mapIndexed { index, item ->
            item.copy(isSelected = index == selectedCategoryIndex)
        }
    }

    // 3. 전체 카드 더미 데이터 (테스트용 100개)
    val allCards = remember {
        List(100) { index ->
            val color = when (index % 7) {
                0 -> Color(0xFFFFF59D) // 노랑
                1 -> Color(0xFFA5D6A7) // 초록
                2 -> Color(0xFF90CAF9) // 파랑
                3 -> Color(0xFFFFCC80) // 주황
                else -> Color(0xFFCE93D8) // 보라
            }
            CardData(text = "단어 ${index + 1}", bgColor = color)
        }
    }

    // 페이지당 카드 개수 변경 (3줄 x 7칸 = 21개)
    var currentPage by remember { mutableIntStateOf(0) }
    val pageSize = 21 // 21로 변경
    val maxPage = if (allCards.isEmpty()) 0 else (allCards.size - 1) / pageSize

    // 현재 페이지 데이터 슬라이싱
    val currentDisplayCards = allCards.drop(currentPage * pageSize).take(pageSize)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // [메인 영역]
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFFF8F8F8))
        ) {
            // 1. 상단 섹션
            TopSection(onNavigateToAiSentence = onNavigateToAiSentence)

            // 2. 카테고리 바 + 설정 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                CategoryBar(
                    categories = categoryList,
                    onCategoryClick = { index ->
                        selectedCategoryIndex = index
                    },
                    modifier = Modifier.weight(1f, fill = false)
                )

                Surface(
                    onClick = { /* 설정 */ },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEEEEEE),
                    border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                    modifier = Modifier.size(width = 92.dp, height = 68.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_setting),
                            contentDescription = "설정",
                            modifier = Modifier.size(32.dp)
                        )
                        Text("설정", fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    }
                }
            }

            // 3. 하단 카드 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .width(1240.dp)
                        .height(480.dp)
                ) {
                    // (A) 카드 리스트
                    CardsArea(
                        cards = currentDisplayCards,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.White)
                    )

                    // (B) 중앙 컨트롤 바
                    CardControlBar(
                        onUpClick = { if (currentPage > 0) currentPage-- },
                        onDownClick = { if (currentPage < maxPage) currentPage++ },
                        modifier = Modifier
                            .width(70.dp)
                            .fillMaxHeight()
                    )

                    // (C) 우측 리액션 사이드바
                    Column(
                        modifier = Modifier
                            .width(92.dp)  //  너비 92dp 고정
                            .height(471.dp) // 높이 471dp 고정
                            .background(Color(0xFFEEEEEE))
                            .padding(6.dp), // 내부 패딩 (버튼과 테두리 사이 간격)
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp) // 버튼 사이 간격
                    ) {
                        // 공통적으로 적용할 Modifier
                        val buttonModifier = Modifier.weight(1f)

                        SmallReactionButton(
                            iconRes = R.drawable.ic_positive,
                            text = "긍정",
                            modifier = buttonModifier
                        )
                        SmallReactionButton(
                            iconRes = R.drawable.ic_negative,
                            text = "부정",
                            modifier = buttonModifier
                        )
                        SmallReactionButton(
                            iconRes = R.drawable.ic_question,
                            text = "질문",
                            modifier = buttonModifier
                        )
                        SmallReactionButton(
                            iconRes = R.drawable.ic_request,
                            text = "부탁",
                            modifier = buttonModifier
                        )
                        SmallReactionButton(
                            iconRes = R.drawable.ic_suggestion,
                            text = "청유",
                            modifier = buttonModifier
                        )
                    }
                }
            }
        }
    }
}

// ... 하단 컴포넌트들 (기존 유지) ...
@Composable
fun TopSection(onNavigateToAiSentence: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .background(Color(215, 230, 249))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CardsSelectedArea(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier.size(92.dp),
            color = Color.Transparent,
            shape = RoundedCornerShape(12.dp),
            onClick = { onNavigateToAiSentence() },
            shadowElevation = 2.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF0080FF), Color(0xFFE8C2EC)),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.btn_ai),
                    contentDescription = "AI문장완성",
                    modifier = Modifier.size(36.dp)
                )
                Text("문장완성", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Surface(
            modifier = Modifier.size(92.dp),
            color = Color(0xFF5E9FFF),
            shape = RoundedCornerShape(12.dp),
            onClick = { },
            shadowElevation = 2.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "재생",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Text("재생", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CardsSelectedArea(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier.fillMaxHeight(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "낱말 카드를 선택하세요.",
                fontSize = 20.sp,
                color = Color(0xFF999999),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun SmallReactionButton(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier // 외부에서 modifier(weight)를 받기 위함
) {
    Column(
        modifier = modifier
            .fillMaxWidth() // 가로는 꽉 채움
            .background(SideBarGray, RoundedCornerShape(6.dp))
            // 높이가 늘어나므로 고정 패딩 대신 내부 여백만 줌
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        //  버튼이 길어져도 내용물(아이콘+글자)은 항상 정중앙에 오도록 설정
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        // 간격 살짝 추가
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Medium)
    }
}