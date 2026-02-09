package com.example.aac.ui.features.auth

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.mapper.toLoginState
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.GuestLoginRequest
import com.example.aac.domain.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _loginState = MutableStateFlow<LoginState?>(null)
    val loginState: StateFlow<LoginState?> = _loginState

    fun startAsGuest() {
        viewModelScope.launch {
            Log.d("AuthTest", "startAsGuest() entered")

            try {
                val deviceId = getDeviceId()
                Log.d("AuthTest", "deviceId = $deviceId")

                // 로그인 API 호출
                val response = RetrofitInstance.api.createGuestAccount(
                    GuestLoginRequest(deviceId = deviceId)
                )

                Log.d("AuthTest", "API response success: ${response.success}")

                if (response.success) {
                    val loginState = response.toLoginState()

                    RetrofitInstance.tokenDataStore.saveAccessToken(loginState.accessToken)

                    Log.d("AuthTest", "토큰 저장 완료! (이제 401 안 뜸): ${loginState.accessToken}")

                    _loginState.value = loginState
                } else {
                    Log.e("AuthTest", "Guest login response failed: ${response.message}")
                }

            } catch (e: Exception) {
                Log.e("AuthTest", "Guest login exception", e)
            }
        }
    }

    private fun getDeviceId(): String {
        val androidId = Settings.Secure.getString(
            getApplication<Application>().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return androidId ?: UUID.randomUUID().toString()
    }
}
