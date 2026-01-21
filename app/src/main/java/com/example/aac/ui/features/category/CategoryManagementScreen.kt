package com.example.aac.ui.features.category

import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.burnoutcrew.reorderable.detectReorder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.ui.features.category.components.*

@Composable
fun CategoryManagementScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val categoryList = remember {
        mutableStateListOf(
            CategoryEditData(iconRes = R.drawable.ic_default, title = "기본", count = 5),
            CategoryEditData(iconRes = R.drawable.ic_human, title = "사람", count = 13),
            CategoryEditData(iconRes = R.drawable.ic_act, title = "행동", count = 13),
            CategoryEditData(iconRes = R.drawable.ic_place, title = "장소", count = 8),
            CategoryEditData(iconRes = R.drawable.ic_emotion, title = "감정", count = 12),
            CategoryEditData(iconRes = R.drawable.ic_food, title = "음식", count = 20)
        )
    }

    // [중요] Reorderable State 선언
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        // 데이터 리스트 순서 변경 로직 (List Swap)
        categoryList.apply {
            add(to.index, removeAt(from.index))
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        CustomTopBar(onBackClick = onBackClick)

        ManagementTabRow(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        LazyColumn(
            state = state.listState, // 스크롤 상태 연결
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .reorderable(state), // reorderable 모디파이어 적용
            contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { TipBox() }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddCategoryButton(onClick = { /* 추가 */ })
                Spacer(modifier = Modifier.height(8.dp))
            }

            // [중요] items 대신 items(list, key) 사용 필수
            items(items = categoryList, key = { it.id }) { item ->

                ReorderableItem(reorderableState = state, key = item.id) { isDragging ->
                    CategoryEditListItem(
                        data = item,
                        isDragging = isDragging, // 드래그 중 시각 효과

                        dragModifier = Modifier.detectReorder(state),

                        onEditClick = { },
                        onDeleteClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onBackClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(CustomBlue, androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("뒤로가기", color = Color.Gray, fontSize = 14.sp)
        }

        Text(
            text = "카테고리 / 낱말 카드 추가",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        TextButton(onClick = { }) {
            Text("저장하기", color = CustomBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
}