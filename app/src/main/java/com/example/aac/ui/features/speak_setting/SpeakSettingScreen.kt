package com.example.aac.ui.features.speak_setting

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.components.CommonSaveDialog
import com.example.aac.ui.features.speak_setting.components.ColumnCountButton
import com.example.aac.ui.features.speak_setting.components.SpeakSettingCardData
import com.example.aac.ui.features.speak_setting.components.SpeakSettingCardItem

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
    onBackClick: () -> Unit = {},
    onSaveClick: (Int) -> Unit = {}
) {
    val initialValue = 7

    var selectedColumnCount by remember { mutableIntStateOf(initialValue) }
    var showSaveDialog by remember { mutableStateOf(false) }

    val hasChanges = selectedColumnCount != initialValue

    val currentStyle = when (selectedColumnCount) {
        7 -> ContainerStyle(323.dp, 43.dp, 23.dp, 130.dp, 20.dp, 12.dp)
        4 -> ContainerStyle(407.dp, 192.dp, 30.dp, 160.dp, 25.dp, 15.dp)
        3 -> ContainerStyle(370.5.dp, 40.dp, 30.dp, 310.dp, 47.dp, 29.dp)
        else -> ContainerStyle(323.dp, 0.dp, 23.dp, 130.dp, 20.dp, 20.dp)
    }

    val fullDummyCards = remember {
        listOf(
            SpeakSettingCardData("나", R.drawable.ic_launcher_foreground, Color(0xFFFFF9C4)),
            SpeakSettingCardData("먹다", R.drawable.ic_launcher_foreground, Color(0xFFC8E6C9)),
            SpeakSettingCardData("가다", R.drawable.ic_launcher_foreground, Color(0xFFC8E6C9)),
            SpeakSettingCardData("크다", R.drawable.ic_launcher_foreground, Color(0xFFBBDEFB)),
            SpeakSettingCardData("학교", R.drawable.ic_launcher_foreground, Color(0xFFFFE0B2)),
            SpeakSettingCardData("위", R.drawable.ic_launcher_foreground, Color(0xFFE1BEE7)),
            SpeakSettingCardData("아래", R.drawable.ic_launcher_foreground, Color(0xFFE1BEE7)),
            SpeakSettingCardData("나", R.drawable.ic_launcher_foreground, Color(0xFFFFF9C4)),
            SpeakSettingCardData("먹다", R.drawable.ic_launcher_foreground, Color(0xFFC8E6C9)),
            SpeakSettingCardData("가다", R.drawable.ic_launcher_foreground, Color(0xFFC8E6C9)),
            SpeakSettingCardData("크다", R.drawable.ic_launcher_foreground, Color(0xFFBBDEFB)),
            SpeakSettingCardData("학교", R.drawable.ic_launcher_foreground, Color(0xFFFFE0B2)),
            SpeakSettingCardData("위", R.drawable.ic_launcher_foreground, Color(0xFFE1BEE7)),
            SpeakSettingCardData("아래", R.drawable.ic_launcher_foreground, Color(0xFFE1BEE7))
        )
    }

    val currentDisplayCards = remember(selectedColumnCount) {
        val countToShow = when (selectedColumnCount) {
            7 -> 14
            4 -> 8
            3 -> 3
            else -> 14
        }
        fullDummyCards.take(countToShow)
    }

    BackHandler {
        if (hasChanges) {
            showSaveDialog = true
        } else {
            onBackClick()
        }
    }

    if (showSaveDialog) {
        CommonSaveDialog(
            message = "변경사항을\n저장하시겠어요?",
            onDismiss = { showSaveDialog = false },
            onSave = {
                onSaveClick(selectedColumnCount)
                showSaveDialog = false
                onBackClick()
            }
        )
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "말하기 화면 설정",
                onBackClick = {
                    if (hasChanges) showSaveDialog = true
                    else onBackClick()
                },
                actionText = "저장하기",
                onActionClick = {
                    onSaveClick(selectedColumnCount)
                    onBackClick()
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(53.dp),
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(selectedColumnCount),
                        horizontalArrangement = Arrangement.spacedBy(currentStyle.gap),
                        verticalArrangement = Arrangement.spacedBy(currentStyle.gap),
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(currentDisplayCards) { card ->
                            SpeakSettingCardItem(
                                card = card,
                                cardSize = currentStyle.cardSize,
                                cardRadius = currentStyle.radius
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun SpeakSettingScreenPreview() {
    SpeakSettingScreen()
}