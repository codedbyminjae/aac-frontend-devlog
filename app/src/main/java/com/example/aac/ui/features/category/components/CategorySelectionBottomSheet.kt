package com.example.aac.ui.features.category.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.aac.ui.features.category.CategoryEditData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionBottomSheet(
    categoryList: List<CategoryEditData>,
    onDismissRequest: () -> Unit,
    onCategorySelected: (CategoryEditData) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedCategory by remember { mutableStateOf<CategoryEditData?>(null) }

    val displayList = remember {
        listOf(
            CategoryEditData(iconRes = R.drawable.ic_default, title = "최근사용", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_favorite, title = "즐겨찾기", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_default, title = "기본", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_human, title = "사람", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_act, title = "행동", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_emotion, title = "감정", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_food, title = "음식", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_place, title = "장소", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_act, title = "신체", count = 0),
            CategoryEditData(iconRes = R.drawable.ic_question, title = "어미", count = 0)
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // [1] 헤더
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            ) {
                Text(
                    text = "낱말 카드를 추가할 카테고리를 선택하세요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        tint = Color.Black
                    )
                }
            }

            // [2] 리스트
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                // items(categoryList) { item ->
                items(displayList) { item ->
                    CategoryItemCard(
                        category = item,
                        isSelected = selectedCategory == item,
                        onClick = { selectedCategory = item }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // [3] 완료 버튼
            Button(
                onClick = {
                    selectedCategory?.let { onCategorySelected(it) } ?: onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "완료",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CategoryItemCard(
    category: CategoryEditData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFFE0E0E0)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Column(
        modifier = Modifier
            .width(80.dp)
            .height(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .border(BorderStroke(borderWidth, borderColor), RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = category.iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}