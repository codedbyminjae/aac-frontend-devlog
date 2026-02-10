package com.example.aac.ui.features.auth

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.mapper.toLoginState
import com.example.aac.data.mapper.toMyInfo
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.GuestLoginRequest
import com.example.aac.domain.model.LoginState
import com.example.aac.domain.model.MyInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.UUID

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    /* ------------------ 로그인 상태 ------------------ */

    private val _loginState = MutableStateFlow<LoginState?>(null)
    val loginState: StateFlow<LoginState?> = _loginState

    /* ------------------ 로그아웃 상태 ------------------ */

    private val _logoutCompleted = MutableStateFlow(false)
    val logoutCompleted: StateFlow<Boolean> = _logoutCompleted


    /* ------------------ 내 정보 (auth/me) ------------------ */

    private val _myInfo = MutableStateFlow<MyInfo?>(null)
    val myInfo: StateFlow<MyInfo?> = _myInfo

    /* ------------------ 게스트 로그인 ------------------ */

    fun startAsGuest() {
        viewModelScope.launch {
            Log.d("AuthTest", "startAsGuest() entered")

            try {
                val deviceId = getDeviceId()
                Log.d("AuthTest", "deviceId = $deviceId")

                // 1. 게스트 로그인 API 호출
                val response = RetrofitInstance.api.createGuestAccount(
                    GuestLoginRequest(deviceId = deviceId)
                )

                Log.d("AuthTest", "API response success: ${response.success}")

                if (response.success) {
                    val loginState = response.toLoginState()

                    // 2. 토큰 저장 (핵심 규칙)
                    RetrofitInstance.tokenDataStore
                        .saveAccessToken(loginState.accessToken)

                    Log.d(
                        "AuthTest",
                        "토큰 저장 완료! (이제 401 안 뜸): ${loginState.accessToken}"
                    )

                    _loginState.value = loginState

                    // 3. 로그인 직후 내 정보 조회 → 토큰 검증
                    fetchMyInfo()

                } else {
                    Log.e(
                        "AuthTest",
                        "Guest login response failed: ${response.message}"
                    )
                }

            } catch (e: Exception) {
                Log.e("AuthTest", "Guest login exception", e)
            }
        }
    }

    /* ------------------ 로그아웃 ------------------ */

    fun logout() {
        viewModelScope.launch {
            try {
                // 1) 서버 로그아웃 (refreshToken 쿠키 제거 목적)
                RetrofitInstance.api.logout()
                Log.d("AuthTest", "서버 로그아웃 호출 완료")

            } catch (e: Exception) {
                // 401 포함, 실패해도 무시
                Log.w("AuthTest", "서버 로그아웃 실패/이미 로그아웃 상태", e)

            } finally {
                // 2) 로컬 토큰 삭제 (진짜 로그아웃)
                RetrofitInstance.tokenDataStore.clearAccessToken()

                // 3) 인증 상태 초기화
                _loginState.value = null
                _myInfo.value = null

                // 4) UI에 로그아웃 완료 알림
                _logoutCompleted.value = true

                Log.d("AuthTest", "로컬 로그아웃 완료")
            }
        }
    }


    /* ------------------ 내 정보 조회 (auth/me) ------------------ */

    fun fetchMyInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMyInfo()

                if (response.success) {
                    _myInfo.value = response.toMyInfo()
                    Log.d(
                        "AuthTest",
                        "내 정보 조회 성공: ${_myInfo.value}"
                    )
                } else {
                    Log.e(
                        "AuthTest",
                        "내 정보 조회 실패: ${response.message}"
                    )
                }

            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.e(
                        "AuthTest",
                        "토큰 만료 또는 미로그인 상태 (401)"
                    )
                    _myInfo.value = null
                    // TODO: 로그아웃 처리 연결
                }
            } catch (e: Exception) {
                Log.e("AuthTest", "내 정보 조회 예외", e)
            }
        }
    }

    /* ------------------ Device ID ------------------ */

    private fun getDeviceId(): String {
        val androidId = Settings.Secure.getString(
            getApplication<Application>().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return androidId ?: UUID.randomUUID().toString()
    }
}
