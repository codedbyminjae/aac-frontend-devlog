package com.example.aac.ui.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aac.R
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.features.settings.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onVoiceSettingClick: () -> Unit,
    onAutoSentenceSettingClick: () -> Unit,
    onUsageHistoryClick: () -> Unit,
    onLogoutSuccess: () -> Unit = {},
    onWithdrawSuccess: () -> Unit = {},
    onCategoryManagementClick: () -> Unit,
    onSpeakSettingClick: () -> Unit
) {

    var showLogoutModal by remember { mutableStateOf(false) }
    var showWithdrawModal by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            CustomTopBar(
                title = "설정",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SettingsAccountCard(
                email = "moduwa@naver.com",
                subscriptionStatus = "프리미엄 구독"
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsQuickActionRow(
                onCategoryClick = onCategoryManagementClick,
                onVoiceClick = onVoiceSettingClick,
                onAutoSentenceClick = onAutoSentenceSettingClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "AAC 설정") {
                SettingsListItem(
                    iconRes = R.drawable.ic_aac_speak,
                    title = "말하기 화면 설정",
                    rightText = "6월",
                    onClick = onSpeakSettingClick
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsListItem(
                    iconRes = R.drawable.ic_record,
                    title = "사용 기록 조회",
                    onClick = onUsageHistoryClick
                )
            }

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

            SettingsSection(title = "계정 정보") {
                SettingsListItem(
                    iconRes = R.drawable.ic_logout2,
                    title = "로그아웃",
                    onClick = { showLogoutModal = true }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsListItem(
                    iconRes = R.drawable.ic_delete_user,
                    title = "회원 탈퇴",
                    onClick = { showWithdrawModal = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showLogoutModal) {
        LogoutConfirmModal(
            onCancel = { showLogoutModal = false },
            onLogout = {
                showLogoutModal = false
                // TODO: 로그아웃 API 연결
                onLogoutSuccess()
            }
        )
    }

    if (showWithdrawModal) {
        WithdrawConfirmModal(
            onCancel = { showWithdrawModal = false },
            onWithdraw = {
                showWithdrawModal = false
                // TODO: 회원탈퇴 API 연결
                onWithdrawSuccess()
            }
        )
    }
}

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
        onAutoSentenceSettingClick = {},
        onUsageHistoryClick = {},
        onCategoryManagementClick = {},
        onSpeakSettingClick = {}
    )
}