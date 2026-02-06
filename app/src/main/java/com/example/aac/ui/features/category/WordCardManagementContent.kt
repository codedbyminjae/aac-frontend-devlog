package com.example.aac.ui.features.category

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.ui.components.CommonDeleteDialog
import com.example.aac.ui.features.category.components.AddWordCardDialog
import com.example.aac.ui.features.category.components.CategorySelectionBottomSheet
import com.example.aac.ui.features.category.components.TipBox
import sh.calvin.reorderable.*

data class WordCardData(
    val id: String,
    val title: String,
    val color: Color
)

@Composable
fun WordCardManagementContent(
    wordList: SnapshotStateList<WordCardData>
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var selectedCategoryName by remember { mutableStateOf("최근사용") }
    var selectedWord by remember { mutableStateOf<WordCardData?>(null) }

    val categoryList = remember {
        listOf(
            CategoryEditData(iconRes = R.drawable.ic_default, title = "최근사용", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_human, title = "사람", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_act, title = "행동", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_place, title = "장소", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_emotion, title = "감정", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_food, title = "음식", count = 0)
        )
    }

    val gridState = rememberLazyGridState()

    val reorderableState = rememberReorderableLazyGridState(gridState) { from, to ->
        if (from.index == 0 || to.index == 0) return@rememberReorderableLazyGridState

        val fromId = from.key as? String
        val toId = to.key as? String
        if (fromId != null && toId != null) {
            val fromIndex = wordList.indexOfFirst { it.id == fromId }
            val toIndex = wordList.indexOfFirst { it.id == toId }
            if (fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
                wordList.apply { add(toIndex, removeAt(fromIndex)) }
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
                currentCategory = selectedCategoryName,
                onClick = { showCategorySheet = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(wordList, key = { it.id }) { item ->
                        if (item.id == "ADD_BUTTON") {
                            AddWordCardItem(onClick = { showAddDialog = true })
                        } else {
                            ReorderableItem(state = reorderableState, key = item.id) { isDragging ->
                                val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "elevation")

                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .shadow(elevation, RoundedCornerShape(12.dp))
                                        .background(item.color, RoundedCornerShape(12.dp))
                                        .draggableHandle()
                                        .clickable {
                                            selectedWord = item
                                            showDeleteDialog = true
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(Color.White.copy(alpha = 0.5f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Dashboard,
                                                contentDescription = null,
                                                tint = Color.Black.copy(alpha = 0.7f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = item.title,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddWordCardDialog(
            onDismissRequest = { showAddDialog = false },
            onSaveClick = { newWord ->
                wordList.add(
                    WordCardData(
                        id = System.currentTimeMillis().toString(),
                        title = newWord,
                        color = Color(0xFFE1BEE7)
                    )
                )
                showAddDialog = false
            }
        )
    }

    if (showCategorySheet) {
        CategorySelectionBottomSheet(
            categoryList = categoryList,
            onDismissRequest = { showCategorySheet = false },
            onCategorySelected = { selectedCategory ->
                selectedCategoryName = selectedCategory.title
                showCategorySheet = false
            }
        )
    }

    if (showDeleteDialog && selectedWord != null) {
        CommonDeleteDialog(
            message = "낱말 카드를\n삭제 하시겠어요?",
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                wordList.remove(selectedWord)
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun AddWordCardItem(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategorySelectorBar(
    currentCategory: String = "최근사용",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(71.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Dashboard,
            contentDescription = null,
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "카테고리 선택",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = currentCategory,
            fontSize = 24.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}