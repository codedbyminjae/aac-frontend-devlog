package com.example.aac.core.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onKakaoLogin = { navController.navigate(Routes.MAIN) },
                onNaverLogin = { navController.navigate(Routes.MAIN) },
                onGoogleLogin = { navController.navigate(Routes.MAIN) },
                onGuestLogin = { navController.navigate(Routes.MAIN) }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onNavigateToAiSentence = {
                    navController.navigate(Routes.AI_SENTENCE)
                },
                onNavigateToSettings = { // 추가
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(Routes.AI_SENTENCE) {
            AiSentenceScreen(
                onBack = { navController.popBackStack() },
                onEditNavigate = { text ->
                    navController.navigate(Routes.aiSentenceEditRoute(text))
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() } // 뒤로가기 → 메인 복귀
            )
        }

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
    }
}
