package com.example.aac.core.navigation

import android.content.Intent // [추가] 액티비티 이동용
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext // [추가] 컨텍스트 획득용
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aac.ui.features.main.MainScreen
import com.example.aac.ui.features.ai_sentence.ui.AiSentenceScreen
import com.example.aac.ui.features.ai_sentence.ui.AiSentenceEditScreen
import com.example.aac.ui.features.login.LoginScreen
import com.example.aac.ui.features.settings.SettingsScreen
import com.example.aac.ui.features.auto_sentence.*
import com.example.aac.ui.features.auto_sentence.AutoSentenceSelectDeleteScreen
import com.example.aac.ui.features.voice_setting.VoiceSettingScreen
import com.example.aac.ui.features.usage_history.UsageHistoryActivity
import com.example.aac.ui.features.category.CategoryManagementScreen
import com.example.aac.ui.features.speak_setting.SpeakSettingScreen
import com.example.aac.ui.features.terms.TermsDetailScreen
import com.example.aac.ui.features.terms.TermsScreen
import com.example.aac.ui.features.login.LoginRoute

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    /* ---------- 자동 문장 리스트 (공용 상태) ---------- */
    var autoSentenceList by remember {
        mutableStateOf(listOf<AutoSentenceItem>())
    }

    // 목소리 설정 정보 반영 데이터
    var voiceSettingId by remember { mutableStateOf("default_male") }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        /* ---------- LOGIN ---------- */
        composable(Routes.LOGIN) {
            LoginRoute(
                onNavigateToTerms = {
                    navController.navigate(Routes.TERMS)
                }
            )
        }

        /* ---------- TERMS ---------- */
        composable(Routes.TERMS) {
            TermsScreen(
                navController = navController,
                onBackClick = {
                    navController.popBackStack()
                },
                onStartClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.TERMS) { inclusive = true }
                    }
                },
                onServiceTermsClick = {
                    navController.navigate(
                        Routes.termsDetailRoute("service")
                    )
                },
                onPrivacyTermsClick = {
                    navController.navigate(
                        Routes.termsDetailRoute("privacy")
                    )
                }
            )
        }

        /* ---------- TERMS DETAIL ---------- */
        composable(
            route = Routes.TERMS_DETAIL_ROUTE,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->

            val type = backStackEntry.arguments?.getString("type") ?: "service"

            TermsDetailScreen(
                type = type,
                onBackClick = { agreed ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("terms_result_$type", agreed)

                    navController.popBackStack()
                }
            )
        }


        /* ---------- MAIN ---------- */
        composable(Routes.MAIN) {
            MainScreen(
                onNavigateToAiSentence = {
                    navController.navigate(Routes.AI_SENTENCE)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        /* ---------- AI SENTENCE ---------- */
        composable(Routes.AI_SENTENCE) {
            AiSentenceScreen(
                onBack = { navController.popBackStack() },
                onEditNavigate = { text ->
                    navController.navigate(Routes.aiSentenceEditRoute(text))
                }
            )
        }

        /* ---------- SETTINGS ---------- */
        composable(Routes.SETTINGS) {
            // [수정됨] 액티비티 실행을 위해 Context 가져오기
            val context = LocalContext.current

            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onAutoSentenceSettingClick = {
                    navController.navigate(Routes.AUTO_SENTENCE_SETTING)
                },
                onVoiceSettingClick = {
                    navController.navigate(Routes.VOICE_SETTING)
                },
                // [추가됨] 사용 기록 조회 클릭 시 -> UsageHistoryActivity 실행
                onUsageHistoryClick = {
                    val intent = Intent(context, UsageHistoryActivity::class.java)
                    context.startActivity(intent)
                },

                onCategoryManagementClick = {
                    navController.navigate(Routes.CATEGORY_MANAGEMENT)
                },

                onSpeakSettingClick = {
                    navController.navigate(Routes.SPEAK_SETTING)
                },

                // 로그아웃 성공 → 초기 화면
                onLogoutSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },

                // 회원탈퇴 성공 → 초기 화면
                onWithdrawSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        /* ---------- VOICE SETTING ---------- */
        composable(Routes.VOICE_SETTING) {
            VoiceSettingScreen(
                initialSelectedId = voiceSettingId,
                onBackClick = { navController.popBackStack() },
                onSave = { selectedId ->
                    voiceSettingId = selectedId
                    // TODO: 나중에 API 저장 연결
                }
            )
        }

        /* ---------- AI SENTENCE EDIT ---------- */
        composable(
            route = Routes.AI_SENTENCE_EDIT_ROUTE,
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val text = backStackEntry.arguments?.getString("text").orEmpty()

            AiSentenceEditScreen(
                initialText = text,
                onBack = { navController.popBackStack() }
            )
        }

        /* ---------- AUTO SENTENCE SETTING ---------- */
        composable(Routes.AUTO_SENTENCE_SETTING) {
            AutoSentenceSettingScreen(
                onBack = { navController.popBackStack() },
                onAddClick = { navController.navigate(Routes.AUTO_SENTENCE_ADD) },
                onEditClick = { item ->
                    navController.navigate(
                        Routes.autoSentenceEditRoute(item.id)
                    )
                },
                onSelectDeleteClick = {
                    navController.navigate(Routes.AUTO_SENTENCE_SELECT_DELETE)
                },
                onDeleteAll = {
                    autoSentenceList = emptyList()
                },
                autoSentenceList = autoSentenceList
            )
        }

        /* ---------- AUTO SENTENCE ADD ---------- */
        composable(Routes.AUTO_SENTENCE_ADD) {
            AutoSentenceAddEditScreen(
                mode = AutoSentenceMode.ADD,
                initialItem = null,
                onBack = { navController.popBackStack() },
                onSave = { item ->
                    autoSentenceList = listOf(item) + autoSentenceList
                    navController.popBackStack()
                }
            )
        }

        /* ---------- AUTO SENTENCE EDIT ---------- */
        composable(
            route = Routes.AUTO_SENTENCE_EDIT,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getLong("itemId")
            val targetItem = autoSentenceList.find { it.id == itemId }

            if (targetItem != null) {
                AutoSentenceAddEditScreen(
                    mode = AutoSentenceMode.EDIT,
                    initialItem = targetItem,
                    onBack = { navController.popBackStack() },

                    onSave = { updatedItem ->
                        autoSentenceList = autoSentenceList.map {
                            if (it.id == targetItem.id) {
                                updatedItem.copy(id = targetItem.id)
                            } else it
                        }
                        navController.popBackStack()
                    },

                    onDelete = {
                        autoSentenceList = autoSentenceList.filter {
                            it.id != targetItem.id
                        }
                        navController.popBackStack()
                    }
                )
            }
        }

        /* ---------- AUTO SENTENCE SELECT DELETE ---------- */
        composable(Routes.AUTO_SENTENCE_SELECT_DELETE) {
            AutoSentenceSelectDeleteScreen(
                autoSentenceList = autoSentenceList,
                onBack = { navController.popBackStack() },
                onDeleteSelected = { selectedIds ->
                    // 선택된 것만 삭제
                    autoSentenceList = autoSentenceList.filterNot { selectedIds.contains(it.id) }
                    navController.popBackStack() // 삭제 후 설정 화면으로 복귀
                }
            )
        }

        composable(Routes.CATEGORY_MANAGEMENT) {
            CategoryManagementScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(Routes.SPEAK_SETTING) {
            SpeakSettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}