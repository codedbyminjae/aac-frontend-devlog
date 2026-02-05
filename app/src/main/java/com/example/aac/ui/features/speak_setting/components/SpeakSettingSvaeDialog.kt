package com.example.aac.ui.features.speak_setting.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SpeakSettingSaveDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
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
                    lineHeight = 40.sp
                )

                Spacer(modifier = Modifier.height(61.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpeakSettingDialogButton(
                        text = "취소",
                        backgroundColor = Color(0xFFE2E5EA),
                        borderColor = Color(0xFFD9D9D9),
                        textColor = Color.Black,
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                    SpeakSettingDialogButton(
                        text = "저장",
                        backgroundColor = Color(0xFF0088FF),
                        borderColor = Color(0xFFD9D9D9),
                        textColor = Color.White,
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SpeakSettingDialogButton(
    text: String,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}