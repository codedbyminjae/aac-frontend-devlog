package com.example.aac.ui.features.category.components

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.aac.R
import com.example.aac.ui.features.category.CategoryEditData

@Composable
fun CategoryEditDialog(
    category: CategoryEditData,
    onDismissRequest: () -> Unit,
    onSaveClick: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf(category.title) }
    var selectedIcon by remember { mutableIntStateOf(category.iconRes) }

    val context = LocalContext.current

    val icons = listOf(
        R.drawable.ic_human, R.drawable.ic_emotion, R.drawable.ic_act,
        R.drawable.ic_hand, R.drawable.ic_pill, R.drawable.ic_hospital, R.drawable.ic_school,
        R.drawable.ic_place, R.drawable.ic_food, R.drawable.ic_paint,
        R.drawable.ic_soccer, R.drawable.ic_song, R.drawable.ic_book
    )

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F7)),
            modifier = Modifier
                .width(530.dp)
                .height(540.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 51.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "카테고리 추가",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.width(426.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "카테고리 이름",
                        fontSize = 18.sp,
                        color = Color(0xFF494949),
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("예: 병원, 학교, 식당...", color = Color.LightGray, fontSize = 18.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp),
                        shape = RoundedCornerShape(5.dp),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            focusedBorderColor = Color(0xFF0088FF),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .width(426.dp)
                        .height(146.42.dp)
                ) {
                    Text(
                        text = "아이콘 선택",
                        fontSize = 18.sp,
                        color = Color(0xFF494949),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.height(117.42.dp),
                        userScrollEnabled = false
                    ) {
                        items(icons) { iconRes ->
                            IconSelectionItem(
                                iconRes = iconRes,
                                isSelected = selectedIcon == iconRes,
                                onClick = { selectedIcon = iconRes }
                            )
                        }
                        item {
                            UploadButtonItem()
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.width(426.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E5EA)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("취소", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                // ✅ 요청하신 디자인 규격이 반영된 토스트 호출
                                showCleanToast(context, "카테고리 이름을 입력해주세요.")
                            } else {
                                onSaveClick(name, selectedIcon)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("저장", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 👇 요청하신 수치(197x42, Radius 10, Padding 반영) 깔끔한 토스트
// ==========================================
fun showCleanToast(context: Context, message: String) {
    val density = context.resources.displayMetrics.density
    val toast = Toast(context)

    val textView = TextView(context).apply {
        text = message
        // 텍스트 크기도 높이(42dp)에 맞춰 적절히 조정
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        setTextColor(AndroidColor.BLACK)
        gravity = Gravity.CENTER

        // 1. Width: 197dp, Height: 42dp 반영
        width = (230 * density).toInt()
        height = (42 * density).toInt()

        // 2. Padding: Top 9, Bottom 9, Left 24, Right 24 반영
        setPadding(
            (24 * density).toInt(),
            (9 * density).toInt(),
            (24 * density).toInt(),
            (9 * density).toInt()
        )

        // 3. 배경 설정 (흰색 + Border Radius 10px + 회색 테두리)
        background = GradientDrawable().apply {
            setColor(AndroidColor.WHITE)
            cornerRadius = 10 * density // radius: 10px
            setStroke(1, AndroidColor.parseColor("#D9D9D9")) // 경계를 위한 연한 테두리
        }
    }

    toast.view = textView
    // 4. 위치: 하단에서 약간 위쪽 (924px 위치와 유사하게 조정 가능하나 기본 Bottom 추천)
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 150)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}

@Composable
fun IconSelectionItem(iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF0088FF) else Color(0xFFD9D9D9),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun UploadButtonItem() {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE2E5EA))
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFFADB5BD),
                    style = stroke,
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_upload),
            contentDescription = null,
            tint = Color.DarkGray,
            modifier = Modifier.size(24.dp)
        )
    }
}