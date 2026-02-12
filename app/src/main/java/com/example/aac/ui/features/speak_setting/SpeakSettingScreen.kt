package com.example.aac.ui.features.speak_setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.components.WordCard
import com.example.aac.ui.components.CommonSaveDialog
import com.example.aac.ui.features.speak_setting.components.ColumnCountButton

data class ContainerStyle(
    val height: Dp,
    val paddingHorizontal: Dp,
    val paddingVertical: Dp,
    val cardSize: Dp,
    val gap: Dp,
    val radius: Dp
)

@Composable
fun SpeakSettingScreen(
    viewModel: SpeakSettingViewModel = viewModel(),
    onBackClick: () -> Unit = {},
) {
    val wordList by viewModel.uiState.collectAsState()
    val serverColumns by viewModel.serverGridColumns.collectAsState()

    var selectedColumnCount by remember { mutableIntStateOf(7) }

    LaunchedEffect(serverColumns) {
        if (serverColumns != 0) {
            selectedColumnCount = serverColumns
        }
    }

    val isChanged = selectedColumnCount != serverColumns

    var showSaveDialog by remember { mutableStateOf(false) }

    val currentStyle = when (selectedColumnCount) {
        7 -> ContainerStyle(323.dp, 43.dp, 23.dp, 130.dp, 20.dp, 12.dp)
        4 -> ContainerStyle(407.dp, 192.dp, 30.dp, 160.dp, 25.dp, 15.dp)
        3 -> ContainerStyle(370.5.dp, 40.dp, 30.dp, 310.dp, 47.dp, 29.dp)
        else -> ContainerStyle(323.dp, 0.dp, 23.dp, 130.dp, 20.dp, 20.dp)
    }

    val currentDisplayCards = remember(selectedColumnCount, wordList) {
        val countToShow = when (selectedColumnCount) {
            7 -> 14
            4 -> 8
            3 -> 3
            else -> 14
        }
        if (wordList.isNotEmpty()) wordList.take(countToShow) else emptyList()
    }

    fun saveAndExit() {
        viewModel.saveGridSetting(selectedColumnCount) {
            onBackClick()
        }
    }

    if (showSaveDialog) {
        CommonSaveDialog(
            message = "변경 사항을\n저장하시겠습니까?",
            onDismiss = { showSaveDialog = false },
            onSave = {
                showSaveDialog = false
                saveAndExit()
            }
        )
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "말하기 화면 설정",
                onBackClick = {
                    if (isChanged) {
                        showSaveDialog = true
                    } else {
                        onBackClick()
                    }
                },
                actionText = "저장하기",
                onActionClick = {
                    saveAndExit()
                }
            )
        },
        containerColor = Color(0xFFF4F4F4)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .width(1116.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "한 줄에 표시할 낱말 카드 수를 설정하세요",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(53.dp),
                    horizontalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    ColumnCountButton("7열", selectedColumnCount == 7, { selectedColumnCount = 7 }, Modifier.weight(1f))
                    ColumnCountButton("4열", selectedColumnCount == 4, { selectedColumnCount = 4 }, Modifier.weight(1f))
                    ColumnCountButton("3열", selectedColumnCount == 3, { selectedColumnCount = 3 }, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(currentStyle.height)
                        .border(BorderStroke(1.dp, Color(0xFFD9D9D9)), RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(
                            top = currentStyle.paddingVertical,
                            bottom = currentStyle.paddingVertical,
                            start = currentStyle.paddingHorizontal,
                            end = currentStyle.paddingHorizontal
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (wordList.isEmpty()) {
                        CircularProgressIndicator()
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(selectedColumnCount),
                            horizontalArrangement = Arrangement.spacedBy(currentStyle.gap),
                            verticalArrangement = Arrangement.spacedBy(currentStyle.gap),
                            userScrollEnabled = false,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(currentDisplayCards) { wordItem ->
                                WordCard(
                                    text = wordItem.word,
                                    imageUrl = wordItem.imageUrl,
                                    partOfSpeech = wordItem.partOfSpeech,
                                    modifier = Modifier.size(currentStyle.cardSize),
                                    cornerRadius = currentStyle.radius,
                                    onClick = { }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}