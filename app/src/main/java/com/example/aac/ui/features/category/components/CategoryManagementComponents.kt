package com.example.aac.ui.features.category.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

// --- 색상 상수 ---
val CustomBlue = Color(0xFF267FD6)
val LightBlueBg = Color(0xFFE3F2FD)
val DeleteRed = Color(0xFFE57373)
val TextGray = Color(0xFF666666)

// --- 데이터 모델 ---
// 순서 변경 시 고유 Key가 필요하므로 id 추가
data class CategoryEditData(
    val id: String = UUID.randomUUID().toString(),
    val iconRes: Int,
    val title: String,
    val count: Int
)

@Composable
fun ManagementTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().height(48.dp).background(Color.White)) {
        TabItem(text = "카테고리 관리", isSelected = selectedTabIndex == 0, onClick = { onTabSelected(0) }, modifier = Modifier.weight(1f))
        TabItem(text = "낱말 카드 관리", isSelected = selectedTabIndex == 1, onClick = { onTabSelected(1) }, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxHeight().clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) CustomBlue else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
        if (isSelected) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(2.dp).background(CustomBlue)
            )
        }
    }
}

@Composable
fun TipBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightBlueBg, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "✨ 팁 : 카테고리를 드래그하여 순서를 변경하실 수 있어요. 자주 사용하는 카테고리를 앞으로 배치해보세요!",
            color = Color(0xFF333333),
            fontSize = 13.sp
        )
    }
}

@Composable
fun AddCategoryButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CustomBlue)
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("카테고리 추가", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CategoryEditListItem(
    data: CategoryEditData,
    isDragging: Boolean = false, // [추가] 드래그 중인지 여부 (스타일 변경용)
    dragModifier: Modifier = Modifier, // [추가] 핸들에 적용할 드래그 감지 모디파이어
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // 드래그 중일 때 살짝 띄워주는 효과 (Shadow & Scale)
    val elevation = if (isDragging) 8.dp else 0.dp
    val bgColor = if (isDragging) Color.White else Color.White // 드래그 중 색상 변경 원하면 수정

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation, RoundedCornerShape(12.dp)) // 그림자 효과
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 드래그 핸들 [수정됨]
        // dragModifier를 여기에 적용해야 이 아이콘을 잡고 움직일 수 있음
        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = "순서 변경",
            tint = Color.Gray,
            modifier = dragModifier
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 2. 카테고리 아이콘
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = data.iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 3. 텍스트 정보
        Column(modifier = Modifier.weight(1f)) {
            Text(text = data.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "${data.count}개의 낱말", fontSize = 12.sp, color = TextGray)
        }

        // 4. 편집 버튼
        EditActionButton(
            text = "편집",
            color = CustomBlue,
            icon = Icons.Default.Edit,
            onClick = onEditClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 5. 삭제 버튼
        EditActionButton(
            text = "삭제",
            color = DeleteRed,
            icon = Icons.Default.Delete,
            onClick = onDeleteClick
        )
    }
}

@Composable
private fun EditActionButton(
    text: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .background(color, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(20.dp))
        Text(text, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}