package com.example.aac.ui.features.flashcard_edit_delete

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.aac.R
import com.example.aac.data.remote.dto.MainWordItem // ✅ DTO 교체
import com.example.aac.ui.components.WordCard // ✅ 공통 컴포넌트 사용
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FlashcardDeleteConfirmDialog(
    card: MainWordItem, // ✅ CardData -> WordItem 변경
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit,
    onConfirmDelete: (MainWordItem) -> Unit // ✅ 타입 변경
) {
    val pointBlue = Color(0xFF0088FF)
    val defaultBorderColor = Color(0xFFD9D9D9)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            modifier = Modifier
                .width(480.dp)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. 카드 이미지 (WordCard 공통 컴포넌트 사용)
                WordCard(
                    text = card.word,
                    imageUrl = card.imageUrl,
                    partOfSpeech = card.partOfSpeech,
                    modifier = Modifier
                        .size(175.dp)
                        .border(1.dp, defaultBorderColor, RoundedCornerShape(16.dp)), // 테두리 적용
                    cornerRadius = 16.dp,
                    onClick = {} // 클릭 동작 없음
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "낱말을 삭제하시겠어요?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = pointBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "삭제한 낱말은 단어 추가에서 추가 가능합니다",
                        fontSize = 16.sp,
                        color = pointBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
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
                        icon = R.drawable.ic_delete_2, // 아이콘 리소스 이름 확인 필요 (ic_delete 또는 ic_delete_2)
                        contentColor = Color.Red,
                        modifier = Modifier.width(200.dp),
                        onClick = {
                            onConfirmDelete(card)
                            // 모달이 닫혀도 스낵바가 보이도록 부모 scope 사용
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