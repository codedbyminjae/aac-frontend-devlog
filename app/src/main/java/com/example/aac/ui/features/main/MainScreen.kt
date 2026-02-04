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
import com.example.aac.ui.features.main.components.*
import com.example.aac.ui.features.flashcard_edit_delete.FlashcardDetailDialog
import kotlinx.coroutines.launch

val SideBarGray = Color(0xFF666666)

@Composable
fun MainScreen(
    onNavigateToAiSentence: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    // 1. 상태 관리
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedDetailCard by remember { mutableStateOf<CardData?>(null) }
    // var selectedEditCard by remember { mutableStateOf<CardData?>(null) } // 필요시 사용

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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

    // 3. 카드 더미 데이터
    val allCards = remember {
        List(100) { index ->
            val color = when (index % 7) {
                0 -> Color(0xFFFFF59D)
                1 -> Color(0xFFA5D6A7)
                2 -> Color(0xFF90CAF9)
                3 -> Color(0xFFFFCC80)
                else -> Color(0xFFCE93D8)
            }
            CardData(text = "단어 ${index + 1}", bgColor = color)
        }
    }

    // 4. 페이지네이션 상태
    var currentPage by remember { mutableIntStateOf(0) }
    val pageSize = 21
    val maxPage = if (allCards.isEmpty()) 0 else (allCards.size - 1) / pageSize
    val currentDisplayCards = allCards.drop(currentPage * pageSize).take(pageSize)

    // [최상위 레이아웃] Box로 감싸서 SnackbarHost 정렬 문제 해결
    Box(modifier = Modifier.fillMaxSize()) {

        // 메인 UI 컨텐츠
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
                        onClick = { onNavigateToSettings() },
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
                                painter = painterResource(id = R.drawable.ic_setting),
                                contentDescription = "설정",
                                modifier = Modifier.size(32.dp)
                            )
                            Text(text = "설정", fontSize = 18.sp, fontWeight = FontWeight.Normal)
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
                            // 클릭 시 선택된 카드를 상태에 저장 -> 다이얼로그 호출 트리거
                            onCardClick = { selectedDetailCard = it },
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
                                .width(92.dp)
                                .height(471.dp)
                                .background(Color(0xFFEEEEEE))
                                .padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
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

        // [스낵바 호스트]
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) { data ->
            val isFavoriteMsg = data.visuals.message.contains("즐겨찾기")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEBEBEB)),
                modifier = Modifier
                    .height(42.dp)
                    .then(
                        if (isFavoriteMsg) Modifier.width(232.dp)
                        else Modifier.wrapContentWidth()
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data.visuals.message,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // [수정됨] 다이얼로그 호출 로직 (주석 해제 및 파라미터 전달)
    if (selectedDetailCard != null) {
        FlashcardDetailDialog(
            card = selectedDetailCard!!,
            onDismiss = { selectedDetailCard = null },
            onEdit = {
                // selectedEditCard = selectedDetailCard
                selectedDetailCard = null
            },
            onDelete = {
                // 삭제 로직 후 스낵바 표시
                val deletedText = selectedDetailCard?.text ?: "카드"
                selectedDetailCard = null
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("'$deletedText' 카드가 삭제되었습니다.")
                }
            },
            // 에러 해결을 위해 추가된 파라미터들
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        )
    }
}

// ... 하단 컴포넌트들은 변경 없음 ...
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SideBarGray, RoundedCornerShape(6.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Medium)
    }
}