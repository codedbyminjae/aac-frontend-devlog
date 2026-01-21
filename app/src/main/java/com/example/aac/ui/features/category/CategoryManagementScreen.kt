package com.example.aac.ui.features.category

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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

import sh.calvin.reorderable.*

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

    val listState = rememberLazyListState()

    val reorderableState = rememberReorderableLazyListState(listState) { from, to ->
        val fromId = from.key as? String
        val toId = to.key as? String

        if (fromId != null && toId != null) {
            val fromIndex = categoryList.indexOfFirst { it.id == fromId }
            val toIndex = categoryList.indexOfFirst { it.id == toId }

            if (fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
                categoryList.apply {
                    add(toIndex, removeAt(fromIndex))
                }
            }
        }
    }

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
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TipBox()
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddCategoryButton(onClick = { /* 추가 로직 */ })
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(items = categoryList, key = { it.id }) { item ->

                ReorderableItem(state = reorderableState, key = item.id) { isDragging ->

                    val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "elevation")

                    CategoryEditListItem(
                        data = item,
                        isDragging = isDragging,

                        dragModifier = Modifier.draggableHandle(),

                        onEditClick = { /* 편집 로직 */ },
                        onDeleteClick = { /* 삭제 로직 */ }
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
                    .background(Color(0xFF267FD6), androidx.compose.foundation.shape.CircleShape),
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
            Text("저장하기", color = Color(0xFF267FD6), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
}