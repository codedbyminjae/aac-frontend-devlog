package com.example.aac.ui.features.flashcard_edit_delete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.aac.R
import com.example.aac.ui.features.main.components.CardData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FlashcardDeleteConfirmDialog(
    card: CardData?,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope, // [중요] 부모(MainScreen)의 스코프를 받음
    onDismiss: () -> Unit,
    onConfirmDelete: (CardData) -> Unit
) {
    if (card == null) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            modifier = Modifier.width(480.dp).wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. 카드 이미지 (175x175)
                Surface(
                    color = card.bgColor,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(175.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_emotion),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = card.text, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "낱말을 삭제하시겠어요?", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFF4A89FF), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "삭제한 낱말은 단어 추가에서 추가 가능합니다", fontSize = 16.sp, color = Color(0xFF4A89FF), fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    // 취소 버튼
                    DetailMenuButton(
                        text = "취소",
                        modifier = Modifier.width(200.dp),
                        onClick = onDismiss
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    // 삭제하기 버튼
                    DetailMenuButton(
                        text = "삭제하기",
                        icon = R.drawable.ic_delete,
                        contentColor = Color.Red,
                        modifier = Modifier.width(200.dp),
                        onClick = {
                            // 1. 즉시 부모 상태 변경 (모달 닫힘)
                            onConfirmDelete(card)

                            // 2. 부모 스코프에서 스낵바 실행 (모달이 닫혀도 취소되지 않음)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("낱말을 삭제했어요.")
                            }
                        }
                    )
                }
            }
        }
    }
}