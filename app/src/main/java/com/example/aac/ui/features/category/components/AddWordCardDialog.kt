package com.example.aac.ui.features.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AddWordCardDialog(
    onDismissRequest: () -> Unit,
    onSaveClick: (String) -> Unit // 입력된 낱말 텍스트 반환
) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // 타이틀
                Text(
                    text = "낱말 카드 추가",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 1. 낱말 입력 필드
                Text(text = "낱말", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("낱말을 입력하세요", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        focusedBorderColor = Color(0xFF267FD6),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. 사진 업로드 영역
                Text(text = "낱말 사진", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp) // 적당한 높이
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE3F2FD)) // 연한 하늘색 배경
                        .clickable { /* 갤러리 연동 로직 */ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Upload",
                            tint = Color(0xFF455A64),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "사진 업로드",
                            color = Color(0xFF267FD6), // 파란색 텍스트
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 3. 버튼 (취소 / 저장)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(48.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("취소", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onSaveClick(text) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF267FD6)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(48.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("저장", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}