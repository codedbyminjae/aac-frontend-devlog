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
