package com.example.aac.ui.features.category.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.domain.model.Category
import com.example.aac.domain.model.Word
import com.example.aac.ui.components.CommonDeleteDialog
import com.example.aac.ui.components.WordCard
import com.example.aac.ui.features.category.CategoryEditData
import kotlinx.coroutines.launch
import sh.calvin.reorderable.*

// ✅ 시스템 기본 폰트 + 피그마 스타일 (15px, 500)
val FigmaTextStyle = TextStyle(
    fontWeight = FontWeight.Medium, // 500
    fontSize = 15.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
    textAlign = TextAlign.Center,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

@Composable
fun WordCardManagementContent(
    categories: List<Category>,
    wordList: List<Word>,
    selectedCategoryId: String?,
    onCategorySelect: (String?) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<Word?>(null) }

    val uiList = remember(wordList) { wordList.toMutableStateList() }

    val currentCategoryName = remember(selectedCategoryId, categories) {
        if (selectedCategoryId == null) "전체"
        else categories.find { it.id == selectedCategoryId }?.name ?: "전체"
    }

    val coroutineScope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val itemsPerPage = 21

    val reorderableState = rememberReorderableLazyGridState(gridState) { from, to ->
        val fromId = from.key as? String
        val toId = to.key as? String
        if (fromId != null && toId != null) {
            val fromIndex = uiList.indexOfFirst { it.cardId == fromId }
            val toIndex = uiList.indexOfFirst { it.cardId == toId }
            if (fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
                uiList.apply { add(toIndex, removeAt(fromIndex)) }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6F8)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .width(1116.dp)
                .fillMaxHeight()
                .padding(vertical = 16.dp)
        ) {
            TipBox(text = "팁 : 낱말 카드를 드래그하여 순서를 변경하실 수 있어요. 자주 사용하는 낱말을 쉽게 찾을 수 있는 위치에 배치해보세요!")

            Spacer(modifier = Modifier.height(16.dp))

            CategorySelectorBar(
                currentCategory = currentCategoryName,
                onClick = { showCategorySheet = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // [좌측] 낱말 카드 그리드 영역
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .width(1057.dp)
                            .height(550.dp)
                    ) {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(7),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            contentPadding = PaddingValues(top = 0.dp, bottom = 20.dp),
                            userScrollEnabled = false,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item(key = "ADD_BUTTON") {
                                AddWordCardItem(onClick = { showAddDialog = true })
                            }
                            items(uiList, key = { it.cardId }) { item ->
                                ReorderableItem(state = reorderableState, key = item.cardId) { isDragging ->
                                    val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "elevation")
                                    WordCard(
                                        text = item.word,
                                        imageUrl = item.imageUrl,
                                        partOfSpeech = item.partOfSpeech,
                                        modifier = Modifier
                                            .size(130.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .shadow(elevation, RoundedCornerShape(12.dp))
                                            .draggableHandle(),
                                        onClick = {
                                            selectedWord = item
                                            showDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // [우측] 스크롤 버튼 영역
                Column(
                    modifier = Modifier
                        .width(51.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    // ▲ 위로 버튼
                    ScrollButton(
                        imageRes = R.drawable.btn_up,
                        label = "위로",
                        modifier = Modifier.weight(1f), // 🔥 남은 공간의 절반 차지
                        onClick = {
                            coroutineScope.launch {
                                val target = (gridState.firstVisibleItemIndex - itemsPerPage).coerceAtLeast(0)
                                gridState.animateScrollToItem(target)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    // ▼ 아래로 버튼
                    ScrollButton(
                        imageRes = R.drawable.btn_down,
                        label = "아래로",
                        modifier = Modifier.weight(1f), // 🔥 남은 공간의 절반 차지
                        onClick = {
                            coroutineScope.launch {
                                val target = gridState.firstVisibleItemIndex + itemsPerPage
                                gridState.animateScrollToItem(target)
                            }
                        }
                    )
                }
            }
        }
    }

    // (다이얼로그 로직)
    if (showAddDialog) {
        AddWordCardDialog(
            onDismissRequest = { showAddDialog = false },
            onSaveClick = { showAddDialog = false }
        )
    }

    if (showCategorySheet) {
        val sheetData = categories.map {
            CategoryEditData(id = it.id, title = it.name, iconRes = R.drawable.ic_default, count = 0)
        }
        CategorySelectionBottomSheet(
            categoryList = sheetData,
            onDismissRequest = { showCategorySheet = false },
            onCategorySelected = { selectedCategory ->
                onCategorySelect(selectedCategory.id)
                showCategorySheet = false
            }
        )
    }

    if (showDeleteDialog && selectedWord != null) {
        CommonDeleteDialog(
            message = "낱말 카드를\n삭제 하시겠어요?",
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                uiList.remove(selectedWord)
                showDeleteDialog = false
            }
        )
    }
}

// ==========================================
// 👇 하위 컴포넌트
// ==========================================

@Composable
fun ScrollButton(
    imageRes: Int,
    label: String,
    modifier: Modifier = Modifier, // ✅ weight를 받기 위한 파라미터 추가
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF66B3FF))
            .clickable(onClick = onClick)
            .padding(vertical = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(25.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                tint = Color.Unspecified,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = label,
            color = Color.White,
            style = FigmaTextStyle
        )
    }
}

@Composable
fun AddWordCardItem(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FA))
            .border(
                BorderStroke(2.dp, Color(0xFFE0E0E0)),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = null,
                tint = Color(0xFF267FD6),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "낱말 추가",
                color = Color(0xFF267FD6),
                style = FigmaTextStyle
            )
        }
    }
}

@Composable
fun CategorySelectorBar(
    currentCategory: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Dashboard, contentDescription = null, tint = Color.Black)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "카테고리 선택",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(text = currentCategory, fontSize = 20.sp, color = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}