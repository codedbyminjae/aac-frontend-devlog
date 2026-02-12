package com.example.aac.core.navigation

import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aac.ui.features.ai_sentence.ui.AiSentenceEditScreen
import com.example.aac.ui.features.auth.AuthViewModel
import com.example.aac.ui.features.auto_sentence.*
import com.example.aac.ui.features.category.CategoryManagementScreen
import com.example.aac.ui.features.main.MainScreen
import com.example.aac.ui.features.settings.SettingsScreen
import com.example.aac.ui.features.speak_setting.SpeakSettingScreen
import com.example.aac.ui.features.terms.TermsDetailScreen
import com.example.aac.ui.features.terms.TermsScreen
import com.example.aac.ui.features.login.LoginRoute
import com.example.aac.ui.features.usage_history.UsageHistoryActivity
import com.example.aac.ui.features.voice_setting.VoiceSettingScreen
import com.example.aac.ui.features.ai_sentence.ui.AiSentenceScreen
import com.example.aac.data.repository.SentenceDataRepository


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    /* ---------- AuthViewModel 단일 생성 ---------- */
    val authViewModel: AuthViewModel = viewModel()

    /* ---------- 자동 문장 리스트 (공용 상태) ---------- */
    var autoSentenceList by remember { mutableStateOf(listOf<AutoSentenceItem>()) }

    /* ---------- 목소리 설정 선택 상태 ---------- */
    var voiceSettingId by remember { mutableStateOf("default_male") }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        /* ---------- LOGIN ---------- */
        composable(Routes.LOGIN) {
            LoginRoute(
                viewModel = authViewModel,
                onNavigateToTerms = {
                    navController.navigate(Routes.TERMS) {
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        /* ---------- TERMS ---------- */
        composable(Routes.TERMS) {
            val signupCompleted by authViewModel.signupCompleted.collectAsState()

            LaunchedEffect(signupCompleted) {
                if (signupCompleted) {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.TERMS) { inclusive = true }
                        launchSingleTop = true
                    }
                    authViewModel.resetSignupState() // 이벤트 소비
                }
            }

            TermsScreen(
                navController = navController,
                authViewModel = authViewModel,
                onBackClick = {
                    // 가입 플로우 취소 (pendingToken 폐기)
                    authViewModel.cancelSignupFlow()

                    // 로그인 화면으로 이동 + Terms 스택 정리
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.TERMS) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        /* ---------- TERMS DETAIL ---------- */
        composable(
            route = "terms_detail/{termId}",
            arguments = listOf(navArgument("termId") { type = NavType.StringType })
        ) { backStackEntry ->
            val termId = backStackEntry.arguments?.getString("termId") ?: ""

            TermsDetailScreen(
                termId = termId,
                authViewModel = authViewModel,
                navController = navController
            )
        }

        /* ---------- MAIN ---------- */
        composable(Routes.MAIN) {
            MainScreen(
                onNavigateToAiSentence = { navController.navigate(Routes.AI_SENTENCE) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        /* ---------- SETTINGS ---------- */
        composable(Routes.SETTINGS) {
            val context = LocalContext.current

            val logoutCompleted by authViewModel.logoutCompleted.collectAsState()
            val withdrawCompleted by authViewModel.withdrawCompleted.collectAsState()

            LaunchedEffect(logoutCompleted) {
                if (logoutCompleted) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                    authViewModel.consumeLogoutCompleted()
                }
            }

            LaunchedEffect(withdrawCompleted) {
                if (withdrawCompleted) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                    authViewModel.consumeWithdrawCompleted()
                }
            }

            SettingsScreen(
                authViewModel = authViewModel,
                onBackClick = { navController.popBackStack() },
                onAutoSentenceSettingClick = { navController.navigate(Routes.AUTO_SENTENCE_SETTING) },
                onVoiceSettingClick = { navController.navigate(Routes.VOICE_SETTING) },
                onUsageHistoryClick = {
                    val intent = Intent(context, UsageHistoryActivity::class.java)
                    context.startActivity(intent)
                },
                onCategoryManagementClick = { navController.navigate(Routes.CATEGORY_MANAGEMENT) },
                onSpeakSettingClick = { navController.navigate(Routes.SPEAK_SETTING) }
            )
        }

        /* ---------- VOICE SETTING ---------- */
        composable(Routes.VOICE_SETTING) {
            VoiceSettingScreen(
                initialSelectedId = voiceSettingId,
                onBackClick = { navController.popBackStack() },
                onSave = { selectedId ->
                    voiceSettingId = selectedId
                }
            )
        }

        composable(Routes.AI_SENTENCE) {
            AiSentenceScreen(
                initialWords = SentenceDataRepository.selectedWords,

                onBack = { navController.popBackStack() },
                onEditNavigate = { text ->
                    navController.navigate(Routes.aiSentenceEditRoute(text))
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
                    navController.navigate(Routes.autoSentenceEditRoute(item.id))
                },
                onSelectDeleteClick = { navController.navigate(Routes.AUTO_SENTENCE_SELECT_DELETE) },
                onDeleteAll = { autoSentenceList = emptyList() },
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
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
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
                            if (it.id == targetItem.id) updatedItem.copy(id = targetItem.id) else it
                        }
                        navController.popBackStack()
                    },
                    onDelete = {
                        autoSentenceList = autoSentenceList.filter { it.id != targetItem.id }
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
                    autoSentenceList = autoSentenceList.filterNot { selectedIds.contains(it.id) }
                    navController.popBackStack()
                }
            )
        }

        /* ---------- CATEGORY MANAGEMENT ---------- */
        composable(Routes.CATEGORY_MANAGEMENT) {
            CategoryManagementScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        /* ---------- SPEAK SETTING ---------- */
        composable(Routes.SPEAK_SETTING) {
            SpeakSettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
