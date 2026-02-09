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

    /**
     * 로그인 없이 바로 시작 (게스트 계정 생성)
     */
    fun startAsGuest() {
        viewModelScope.launch {
            Log.d("AuthTest", "startAsGuest() entered")

            try {
                val deviceId = getDeviceId()
                Log.d("AuthTest", "deviceId = $deviceId")

                val response = RetrofitInstance.api.createGuestAccount(
                    GuestLoginRequest(deviceId = deviceId)
                )

                Log.d("AuthTest", "API response received")

                if (response.success) {
                    _loginState.value = response.toLoginState()
                }

            } catch (e: Exception) {
                Log.e("AuthTest", "Guest login failed", e)
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
