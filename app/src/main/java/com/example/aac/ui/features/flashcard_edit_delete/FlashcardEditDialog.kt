package com.example.aac.ui.features.flashcard_edit_delete

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.aac.R
import com.example.aac.ui.features.main.components.CardData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardEditDialog(
    card: CardData?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    if (card == null) return

    var wordText by remember { mutableStateOf(card.text) }
    var selectedCategory by remember { mutableStateOf("감정") }
    var isEditingWord by remember { mutableStateOf(false) }

    var showPhotoSheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val photoSheetState = rememberModalBottomSheetState()
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pointBlue = Color(0xFF0088FF)
    val lightGrayBorder = Color(0xFFDDDDDD)
    val buttonBorderColor = Color(0xFFD9D9D9)

    LaunchedEffect(isEditingWord) {
        if (isEditingWord) {
            delay(100)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier
                    .width(520.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 51.dp, top = 48.dp, end = 51.dp, bottom = 48.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "낱말 카드 수정",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 24.dp)
                    )

                    Column(modifier = Modifier.width(426.dp)) {
                        Text(text = "카테고리", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.fillMaxWidth().height(51.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = lightGrayBorder,
                                unfocusedBorderColor = lightGrayBorder,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            trailingIcon = { ChangeButton(onClick = { showCategorySheet = true }) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.width(426.dp)) {
                        Text(text = "낱말", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = wordText,
                            onValueChange = { if (isEditingWord) wordText = it },
                            readOnly = !isEditingWord,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.fillMaxWidth().height(51.dp).focusRequester(focusRequester),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (isEditingWord) pointBlue else lightGrayBorder,
                                unfocusedBorderColor = lightGrayBorder,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            trailingIcon = {
                                if (isEditingWord) {
                                    IconButton(onClick = { wordText = "" }) {
                                        Icon(imageVector = Icons.Default.Clear, contentDescription = "초기화")
                                    }
                                } else {
                                    ChangeButton(onClick = { isEditingWord = true })
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                isEditingWord = false
                                keyboardController?.hide()
                            })
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier.width(426.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "낱말 사진",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFFDE7))
                                .drawBehind {
                                    drawRoundRect(
                                        color = pointBlue,
                                        style = Stroke(
                                            width = 1.5.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                        ),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                                    )
                                }
                                .clickable { showPhotoSheet = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(painterResource(id = R.drawable.ic_emotion), null, Modifier.size(80.dp), Color.Unspecified)
                            Icon(Icons.Default.FileUpload, null, Modifier.size(48.dp), Color(0xFF333333))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "사진 업로드",
                            fontSize = 18.sp,
                            color = pointBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.width(426.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, buttonBorderColor)
                        ) {
                            Text(text = "취소", color = Color.Black, fontSize = 18.sp)
                        }

                        Button(
                            onClick = {
                                if (wordText.trim().isEmpty()) {
                                    scope.launch { snackbarHostState.showSnackbar("낱말을 입력해주세요.") }
                                } else {
                                    onSave(wordText)
                                }
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = pointBlue),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, buttonBorderColor)
                        ) {
                            Text(text = "저장", color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
            ) { data ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEBEBEB)),
                    modifier = Modifier.height(42.dp).width(214.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = data.visuals.message, color = Color.Black, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
        }
    }

    if (showPhotoSheet) {
        ModalBottomSheet(onDismissRequest = { showPhotoSheet = false }, sheetState = photoSheetState, containerColor = Color.White) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 40.dp)) {
                Text(text = "사진 업로드", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
                PhotoOptionItem(Icons.Default.PhotoLibrary, "사진에서 불러오기") { showPhotoSheet = false }
                PhotoOptionItem(Icons.Default.AddAPhoto, "카메라로 촬영하기") { showPhotoSheet = false }
            }
        }
    }

    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false },
            sheetState = categorySheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            CategorySelectionContent(
                currentCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                onComplete = { showCategorySheet = false }
            )
        }
    }
}

@Composable
fun CategorySelectionContent(
    currentCategory: String,
    onCategorySelected: (String) -> Unit,
    onComplete: () -> Unit
) {
    val pointBlue = Color(0xFF0088FF)
    val categories = listOf(
        "최근사용" to R.drawable.ic_recent_use, "즐겨찾기" to R.drawable.ic_favorite,
        "기본" to R.drawable.ic_default, "사람" to R.drawable.ic_human,
        "행동" to R.drawable.ic_act, "감정" to R.drawable.ic_emotion,
        "음식" to R.drawable.ic_food, "장소" to R.drawable.ic_place,
        "신체" to R.drawable.ic_human, "어미" to R.drawable.ic_question
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // [에러 수정 포인트] padding 안에 horizontal 명시
        Text(
            text = "낱말 카드를 추가할 카테고리를 선택하세요",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp).padding(horizontal = 24.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEach { (name, icon) ->
                val isSelected = name == currentCategory
                CategoryButton(
                    name = name,
                    icon = icon,
                    isSelected = isSelected,
                    pointBlue = pointBlue,
                    onClick = { onCategorySelected(name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = pointBlue),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFD9D9D9))
        ) {
            Text("완료", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CategoryButton(
    name: String,
    icon: Int,
    isSelected: Boolean,
    pointBlue: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(84.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) pointBlue else Color(0xFFEEEEEE),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = name,
            modifier = Modifier.size(32.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 13.sp,
            color = if (isSelected) pointBlue else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ChangeButton(onClick: () -> Unit) {
    val grayColor = Color(0xFF494949)
    Row(
        modifier = Modifier.padding(end = 20.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(id = R.drawable.ic_edit), null, Modifier.size(18.dp), grayColor)
        Spacer(Modifier.width(4.dp))
        Text("변경", fontSize = 14.sp, color = grayColor)
    }
}

@Composable
fun PhotoOptionItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(28.dp), Color.Gray)
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 18.sp)
    }
}