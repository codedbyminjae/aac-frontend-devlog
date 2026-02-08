package com.example.aac.ui.features.ai_sentence.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aac.R
import com.example.aac.feature.ai_sentence.ui.components.SentenceCard
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.theme.AacTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSentenceScreen(
    onBack: () -> Unit,
    onEditNavigate: (String) -> Unit,
    vm: AiSentenceViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    val skyBlue = Color(0xFF66B2FF)
    val lightGrayBg = Color(0xFFF4F4F4)
    val grayButton = Color(0xFF666666)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isBanmalMode by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "AI 문장 완성",
                onBackClick = onBack
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .height(42.dp)
                        .widthIn(min = 232.dp)
                        .background(
                            color = Color(0xFFEEEEEE),
                            shape = RoundedCornerShape(21.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data.visuals.message,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        },
        containerColor = lightGrayBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. 상단 라벨 영역
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "선택한 낱말",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "반말",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = isBanmalMode,
                        onCheckedChange = {
                            isBanmalMode = it
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val msg = if (it) "반말 모드로 변경했어요." else "존댓말 모드로 변경했어요."
                                snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = skyBlue,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
            }

            // 2. 상단 컨테이너
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 2-1. 낱말 리스트
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MockWordItem("밥", Color(0xFFFFE082))
                        MockWordItem("먹다", Color(0xFFA5D6A7))
                        MockWordItem("긍정", Color(0xFF666666), isDark = true)
                    }

                    // 2-2. 버튼 그룹
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TopSquareButton(
                            text = "새로고침",
                            iconRes = R.drawable.ic_refresh,
                            backgroundColor = grayButton
                        ) {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("새로고침 했어요.", duration = SnackbarDuration.Short)
                            }
                        }

                        TopSquareButton(
                            text = "재생",
                            iconRes = R.drawable.ic_play,
                            backgroundColor = skyBlue
                        ) {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("낱말을 재생했어요.", duration = SnackbarDuration.Short)
                            }
                            vm.onEvent(AiSentenceUiEvent.ClickPlayTop)
                        }
                    }
                }
            }

            // 3. 문장 리스트
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.sentences, key = { it.id }) { item ->
                    SentenceCard(
                        text = item.text,
                        isFavorite = item.isFavorite,
                        onEdit = {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("편집화면으로 넘어갈게요.", duration = SnackbarDuration.Short)
                            }
                            onEditNavigate(item.text)
                        },
                        onFavorite = {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("즐겨찾기에 추가했어요.", duration = SnackbarDuration.Short)
                            }
                            vm.onEvent(AiSentenceUiEvent.ClickFavorite(item.id))
                        },
                        onPlay = {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("문장을 재생했어요.", duration = SnackbarDuration.Short)
                            }
                            vm.onEvent(AiSentenceUiEvent.ClickPlaySentence(item.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MockWordItem(label: String, color: Color, isDark: Boolean = false) {
    Surface(
        color = color,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(86.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.5f))
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun TopSquareButton(
    text: String,
    iconRes: Int,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(86.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(
    name = "Figma Design Size",
    device = "spec:width=1280dp,height=720dp,dpi=320,orientation=landscape",
    showBackground = true
)
@Composable
fun AiSentencesScreenPreview() {
    AacTheme {
        AiSentenceScreen(
            onBack = {},
            onEditNavigate = {}
        )
    }
}