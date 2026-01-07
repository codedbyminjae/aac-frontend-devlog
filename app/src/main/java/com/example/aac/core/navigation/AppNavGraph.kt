package com.example.aac.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aac.feature.ai_sentence.ui.AiSentenceScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AI_SENTENCE
    ) {
        composable(Routes.AI_SENTENCE) {
            AiSentenceScreen(
                onBack = { /* navController.popBackStack() 필요 시 */ }
            )
        }
    }
}
