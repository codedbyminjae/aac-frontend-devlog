package com.example.aac.ui.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import com.example.aac.ui.features.settings.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onVoiceSettingClick: () -> Unit,
    onAutoSentenceSettingClick: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            SettingsTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 계정 카드
            SettingsAccountCard(
                email = "moduwa@naver.com",
                subscriptionStatus = "프리미엄 구독"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3개 퀵액션 카드
            SettingsQuickActionRow(
                onCategoryClick = { /* TODO */ },
                onVoiceClick = onVoiceSettingClick,
                onAutoSentenceClick = onAutoSentenceSettingClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* ==========================================================
               AAC 설정 섹션
            ========================================================== */
            SettingsSection(title = "AAC 설정") {
                SettingsListItem(
                    iconRes = R.drawable.ic_aac_speak,
                    title = "말하기 화면 설정",
                    rightText = "6월",
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsListItem(
                    iconRes = R.drawable.ic_record,
                    title = "사용 기록 조회",
                    onClick = { /* TODO */ }
                )
            }

            /* ==========================================================
               앱 지원 섹션
            ========================================================== */
            SettingsSection(title = "앱 지원") {
                SettingsListItem(
                    iconRes = R.drawable.ic_help,
                    title = "도움말",
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsListItem(
                    iconRes = R.drawable.ic_info,
                    title = "버전 정보",
                    onClick = { /* TODO */ }
                )
            }

            /* ==========================================================
               계정 정보 섹션
            ========================================================== */
            SettingsSection(title = "계정 정보") {
                SettingsListItem(
                    iconRes = R.drawable.ic_logout,
                    title = "로그아웃",
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsListItem(
                    iconRes = R.drawable.ic_delete_user,
                    title = "회원 탈퇴",
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* ======================================================
   SettingsTopBar
   ====================================================== */
@Composable
fun SettingsTopBar(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 12.dp, start = 24.dp, end = 24.dp)
    ) {
        // 중앙 타이틀
        Text(
            text = "설정",
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            color = Color(0xFF2C2C2C),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )

        // 좌측 뒤로가기
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_circle),
                contentDescription = "Back",
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "뒤로가기",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color(0xFF373737)
            )
        }
    }
}

/* ======================================================
   Preview
   ====================================================== */
@Preview(
    name = "Settings Landscape (1280x1086)",
    showBackground = true,
    widthDp = 1280,
    heightDp = 1086,
    device = "spec:width=1280dp,height=1086dp,orientation=landscape"
)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        onBackClick = {},
        onVoiceSettingClick = {},
        onAutoSentenceSettingClick = {}
    )
}
