package com.example.aac.ui.features.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.aac.R

@Composable
fun WithdrawConfirmModal(
    onCancel: () -> Unit,
    onWithdraw: () -> Unit
) {
    Dialog(onDismissRequest = { /* dismiss 막음 */ }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(451.dp)
                    .height(323.dp)
                    .background(
                        color = Color(0xFFF3F4F7),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(
                        start = 51.dp,
                        end = 51.dp,
                        top = 64.dp,
                        bottom = 48.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /* ===== Title ===== */
                Text(
                    text = "정말 탈퇴하시겠어요?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                /* ===== Warning Text ===== */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_warning),
                        contentDescription = null,
                        tint = Color(0xFF267FD6),
                        modifier = Modifier.size(22.dp)
                    )

                    Text(
                        text = "모든 기록은 영구 삭제되며 복구할 수 없어요.",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.Normal, // Regular
                        color = Color(0xFF267FD6),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(61.dp))

                /* ===== Buttons (로그아웃 모달과 동일) ===== */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {

                    // ---------- 취소 ----------
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .background(
                                color = Color(0xFFE2E5EA),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD9D9D9),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onCancel() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "취소",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    // ---------- 회원탈퇴 ----------
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .background(
                                color = Color(0xFF0088FF),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD9D9D9),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onWithdraw() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logout),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "회원탈퇴",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
