package com.example.aac.ui.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    Row(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // [왼쪽 영역] 상단바 + 카테고리 + 카드 영역
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // 1, 2, 5, 6번 영역: 상단 검색 및 버튼바
            TopSection()

            // 3번 영역: 카테고리 탭 (임시)
            CategoryRow()

            // 4번 영역: 카드 리스트 (현재 비워둠)
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Text("카드 영역", modifier = Modifier.align(Alignment.Center))
            }
        }

        // [오른쪽 영역] 7, 8번 사이드바
        RightSideBar()
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier.fillMaxWidth().height(70.dp).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = {}) { Text("키보드") }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.weight(1f).fillMaxHeight()
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("낱말 카드를 선택해주세요", color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {}) { Text("AI") }
        Button(onClick = {}) { Text("재생") }
    }
}

@Composable
fun CategoryRow() {
    Row(modifier = Modifier.fillMaxWidth().height(50.dp).background(Color(0xFFE0E0E0))) {
        val categories = listOf("최근사용", "즐겨찾기", "밥", "학교", "병원", "교통", "놀이")
        categories.forEach { name ->
            Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                Text(name, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun RightSideBar() {
    Column(
        modifier = Modifier.width(100.dp).fillMaxHeight().background(Color(0xFFF5F5F5)).padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = {}) { Text("설정") }
        Spacer(modifier = Modifier.weight(1f))
        val reactions = listOf("좋아요", "싫어요", "질문", "해주세요", "합시다")
        reactions.forEach { text ->
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text(text) }
        }
    }
}