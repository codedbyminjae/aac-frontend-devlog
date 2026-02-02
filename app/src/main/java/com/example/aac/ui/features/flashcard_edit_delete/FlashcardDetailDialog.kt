package com.example.aac.ui.features.flashcard_edit_delete

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun FlashcardDetailDialog(
    card: CardData?,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit,
    onPlay: (CardData) -> Unit = {},
    onFavorite: (CardData, Boolean) -> Unit = { _, _ -> },
    onEdit: (CardData) -> Unit = {},
    onDelete: (CardData) -> Unit = {}
) {
    if (card == null) return

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    val localSnackbarHostState = remember { SnackbarHostState() }
    val pointBlue = Color(0xFF0088FF)
    val defaultBorderColor = Color(0xFFD9D9D9) // #D9D9D9

    if (showDeleteConfirm) {
        FlashcardDeleteConfirmDialog(
            card = card,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            onDismiss = { showDeleteConfirm = false },
            onConfirmDelete = {
                onDelete(it)
                showDeleteConfirm = false
                onDismiss()
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                modifier = Modifier.size(width = 530.dp, height = 603.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 51.dp, top = 32.dp, end = 51.dp, bottom = 48.dp)
                ) {
                    // [닫기 버튼] 5px 테두리 및 #666666 색상 적용
                    Surface(
                        onClick = onDismiss,
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(width = 1.dp, color = Color(0xFF666666)), // 1px 테두리
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(30.dp) // 바깥 원 크기 30x30
                    ) {
                        // Box를 추가해서 내부 아이콘을 정중앙(Center)에 배치합니다.
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "닫기",
                                modifier = Modifier.size(15.dp), // X 이미지 크기 10x10
                                tint = Color.Gray
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))

                        // [카드 낱말] 1px 테두리 및 #D9D9D9 색상 적용
                        Surface(
                            color = card.bgColor,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, defaultBorderColor),
                            modifier = Modifier.size(175.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(painterResource(R.drawable.ic_emotion), null, Modifier.size(80.dp), Color.Unspecified)
                                Spacer(Modifier.height(12.dp))
                                Text(card.text, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // [메뉴 버튼들] 공통적으로 1px #D9D9D9 테두리 적용
                        DetailMenuButton(text = "재생", icon = R.drawable.ic_play, containerColor = pointBlue, contentColor = Color.White, useDefaultInteraction = false, onClick = { onPlay(card) })
                        Spacer(modifier = Modifier.height(12.dp))

                        DetailMenuButton(
                            text = "즐겨찾기",
                            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            iconTint = pointBlue,
                            onClick = {
                                isFavorite = !isFavorite
                                coroutineScope.launch { localSnackbarHostState.showSnackbar(if (isFavorite) "즐겨찾기에 추가했어요." else "즐겨찾기를 해제했어요.") }
                                onFavorite(card, isFavorite)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        DetailMenuButton(text = "수정하기", icon = R.drawable.ic_edit_2, iconTint = pointBlue, onClick = { onEdit(card) })
                        Spacer(modifier = Modifier.height(12.dp))

                        DetailMenuButton(text = "삭제하기", icon = R.drawable.ic_delete_2, contentColor = Color.Red, onClick = { showDeleteConfirm = true })

                        Spacer(modifier = Modifier.weight(1.2f))
                    }
                }
            }

            SnackbarHost(
                hostState = localSnackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp)
            ) { data ->
                val isFavMsg = data.visuals.message.contains("즐겨찾기")
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
                    modifier = Modifier.height(42.dp).then(if (isFavMsg) Modifier.width(232.dp) else Modifier.wrapContentWidth())
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(data.visuals.message, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardDeleteConfirmDialog(card: CardData, snackbarHostState: SnackbarHostState, coroutineScope: CoroutineScope, onDismiss: () -> Unit, onConfirmDelete: (CardData) -> Unit) {
    val pointBlue = Color(0xFF0088FF)
    val defaultBorderColor = Color(0xFFD9D9D9)

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(32.dp), color = Color.White, modifier = Modifier.width(480.dp).wrapContentHeight()) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = card.bgColor,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, defaultBorderColor),
                    modifier = Modifier.size(175.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(painterResource(R.drawable.ic_emotion), null, Modifier.size(64.dp), Color.Unspecified)
                        Spacer(Modifier.height(8.dp))
                        Text(card.text, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text("낱말을 삭제하시겠어요?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = pointBlue, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("삭제한 낱말은 단어 추가에서 추가 가능합니다", fontSize = 16.sp, color = pointBlue)
                }
                Spacer(Modifier.height(32.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                    DetailMenuButton(text = "취소", modifier = Modifier.width(200.dp), onClick = onDismiss)
                    Spacer(Modifier.width(12.dp))
                    DetailMenuButton(text = "삭제하기", icon = R.drawable.ic_delete_2, contentColor = Color.Red, modifier = Modifier.width(200.dp), onClick = {
                        onConfirmDelete(card)
                        coroutineScope.launch { snackbarHostState.showSnackbar("낱말을 삭제했어요.") }
                    })
                }
            }
        }
    }
}

@Composable
fun DetailMenuButton(text: String, modifier: Modifier = Modifier, icon: Int? = null, imageVector: ImageVector? = null, containerColor: Color? = null, contentColor: Color = Color.Black, iconTint: Color? = null, useDefaultInteraction: Boolean = true, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (useDefaultInteraction) { if (isPressed) Color(0xFFC4CAD4) else Color(0xFFE2E5EA) } else { containerColor ?: Color(0xFFE2E5EA) }

    // 버튼 테두리 설정
    val borderColor = Color(0xFFD9D9D9)

    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier.then(if (modifier == Modifier) Modifier.fillMaxWidth() else Modifier).height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor), // 1px Solid Border 적용
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (icon != null) Icon(painterResource(icon), null, Modifier.size(24.dp), iconTint ?: contentColor)
            else if (imageVector != null) Icon(imageVector, null, Modifier.size(24.dp), iconTint ?: contentColor)
            Spacer(Modifier.width(10.dp))
            Text(text, color = contentColor, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }
    }
}