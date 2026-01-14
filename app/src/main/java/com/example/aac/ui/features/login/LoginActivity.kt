package com.example.aac.ui.features.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aac.ui.theme.AacTheme

@Deprecated("Use MainActivity with Compose Navigation instead")
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AacTheme {
                LoginScreen(
                    onKakaoLogin = { },
                    onNaverLogin = { },
                    onGoogleLogin = { },
                    onGuestLogin = { }
                )
            }
        }
    }
}
