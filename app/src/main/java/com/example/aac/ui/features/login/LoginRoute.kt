package com.example.aac.ui.features.login

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.aac.ui.features.auth.AuthViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

private const val TAG = "KAKAO_FLOW"

@Composable
fun LoginRoute(
    viewModel: AuthViewModel,
    onNavigateToTerms: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val context = LocalContext.current

    val loginState by viewModel.loginState.collectAsState()
    val pendingToken by viewModel.pendingToken.collectAsState()

    LoginScreen(
        onSocialLoginClick = {
            Log.d(TAG, "LoginScreen: 소셜 로그인 버튼 클릭")

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "Kakao callback: 로그인 실패", error)
                } else if (token != null) {
                    Log.d(TAG, "Kakao callback: accessToken received (len=${token.accessToken.length})")

                    // 카카오 accessToken → 서버 전달
                    Log.d(TAG, "Send to server: kakaoLogin() 호출")
                    viewModel.kakaoLogin(token.accessToken)
                } else {
                    Log.w(TAG, "Kakao callback: token=null, error=null (unexpected)")
                }
            }

            val talkAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
            Log.d(TAG, "KakaoTalk available? $talkAvailable")

            if (talkAvailable) {
                Log.d(TAG, "Start loginWithKakaoTalk()")
                UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
            } else {
                Log.d(TAG, "Start loginWithKakaoAccount()")
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        },

        onGuestLoginClick = {
            Log.d(TAG, "LoginScreen: 게스트 로그인 버튼 클릭")
            viewModel.startAsGuest()
        }
    )

    /* 신규 회원 → 약관 화면 */
    LaunchedEffect(pendingToken) {
        Log.d(TAG, "pendingToken changed: ${pendingToken?.take(10)}... (null? ${pendingToken == null})")
        if (pendingToken != null) {
            Log.d(TAG, "Navigate -> TERMS")
            onNavigateToTerms()
        }
    }

    /* 기존 회원/게스트 → 메인 */
    LaunchedEffect(loginState) {
        Log.d(TAG, "loginState changed: ${loginState != null}")
        if (loginState != null) {
            Log.d(TAG, "Navigate -> MAIN")
            onNavigateToMain()
        }
    }
}
