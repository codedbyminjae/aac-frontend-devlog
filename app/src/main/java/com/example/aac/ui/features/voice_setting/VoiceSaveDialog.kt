package com.example.aac.ui.features.voice_setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun VoiceSaveDialog(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = modifier
                .width(451.dp)
                .height(317.dp)
                .background(Color.White, RoundedCornerShape(32.dp))
                .padding(
                    start = 51.dp,
                    end = 51.dp,
                    top = 64.dp,
                    bottom = 48.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "변경 사항을\n저장 하시겠어요?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C2C2C),
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp // 줄간격은 피그마 값 없어서 보기 좋은 값으로(필요하면 조정)
                )

                Spacer(modifier = Modifier.height(61.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialogButton(
                        text = "취소",
                        background = Color(0xFFE2E5EA),
                        borderColor = Color(0xFFD9D9D9),
                        textColor = Color.Black,
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    )

                    DialogButton(
                        text = "저장",
                        background = Color(0xFF0088FF),
                        borderColor = Color(0xFFD9D9D9),
                        textColor = Color.White,
                        onClick = onSave,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    background: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(background, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 21.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

