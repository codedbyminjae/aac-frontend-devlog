package com.example.aac.ui.features.login


import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aac.ui.features.auth.AuthViewModel
import com.example.aac.ui.features.login.LoginScreen

@Composable
fun LoginRoute(
    onNavigateToTerms: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel: AuthViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(application)
    )

    val loginState by viewModel.loginState.collectAsState()

    LoginScreen(
        onSocialLoginClick = {
            onNavigateToTerms()
        },
        onGuestLoginClick = {
            Log.d("AuthTest", "Guest login button clicked")
            viewModel.startAsGuest()
        }
    )

    LaunchedEffect(Unit) {
        snapshotFlow { loginState }
            .collect { state ->
                if (state != null) {
                    Log.d("AuthTest", "Guest login success: $state")
                    onNavigateToTerms()
                }
            }
    }
}
