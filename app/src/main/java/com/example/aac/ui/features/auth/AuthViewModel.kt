package com.example.aac.ui.features.auth

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.mapper.toLoginState
import com.example.aac.data.mapper.toMyInfo
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.*
import com.example.aac.domain.model.LoginState
import com.example.aac.domain.model.MyInfo
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.UUID

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    /* ------------------ STATE ------------------ */
    private val _loginState = MutableStateFlow<LoginState?>(null)
    val loginState: StateFlow<LoginState?> = _loginState

    private val _pendingToken = MutableStateFlow<String?>(null)
    val pendingToken: StateFlow<String?> = _pendingToken

    private val _termsList = MutableStateFlow<List<TermsResponse>>(emptyList())
    val termsList: StateFlow<List<TermsResponse>> = _termsList

    private val _logoutCompleted = MutableStateFlow(false)
    val logoutCompleted: StateFlow<Boolean> = _logoutCompleted

    private val _withdrawCompleted = MutableStateFlow(false)
    val withdrawCompleted: StateFlow<Boolean> = _withdrawCompleted

    private val _signupCompleted = MutableStateFlow(false)
    val signupCompleted: StateFlow<Boolean> = _signupCompleted

    private val _myInfo = MutableStateFlow<MyInfo?>(null)
    val myInfo: StateFlow<MyInfo?> = _myInfo

    /* ------------------ TERMS ------------------ */
    fun fetchTerms() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTerms()
                if (response.success) {
                    _termsList.value = response.data ?: emptyList()
                    Log.d("AUTH", "약관 조회 성공: ${_termsList.value.size}개")
                }
            } catch (e: Exception) {
                Log.e("AUTH", "약관 조회 실패", e)
            }
        }
    }

    fun cancelSignupFlow() {
        Log.d("AUTH", "cancelSignupFlow() called: pendingToken cleared")
        _pendingToken.value = null
        _termsList.value = emptyList()
        _signupCompleted.value = false
    }

    /* ------------------ KAKAO LOGIN ------------------ */
    fun kakaoLogin(kakaoAccessToken: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.kakaoLogin(
                    KakaoLoginRequest(kakaoAccessToken)
                )

                if (response.success) {

                    response.data?.pendingToken?.let {
                        _pendingToken.value = it
                        Log.d("AUTH", "신규 회원 - 약관 동의 필요")
                        return@launch
                    }

                    handleLoginSuccess(response.toLoginState())
                }

            } catch (e: Exception) {
                Log.e("AUTH", "카카오 로그인 실패", e)
            }
        }
    }

    /* ------------------ SIGNUP COMPLETE ------------------ */
    fun completeSocialSignup() {
        viewModelScope.launch {

            val token = _pendingToken.value ?: return@launch
            val terms = _termsList.value

            if (terms.isEmpty()) {
                Log.e("AUTH", "약관 목록이 비어 있음")
                return@launch
            }

            val agreements = terms.map {
                AgreementDto(
                    termsId = it.id,
                    isAgreed = true
                )
            }

            try {
                val response = RetrofitInstance.api.completeSocialSignup(
                    SocialCompleteRequest(
                        pendingToken = token,
                        agreements = agreements
                    )
                )

                if (response.success) {
                    handleLoginSuccess(response.toLoginState())
                    _pendingToken.value = null
                    _signupCompleted.value = true
                    Log.d("AUTH", "회원가입 완료 성공")
                }

            } catch (e: Exception) {
                Log.e("AUTH", "회원가입 완료 실패", e)
            }
        }
    }

    /* ------------------ GUEST LOGIN ------------------ */
    fun startAsGuest() {
        viewModelScope.launch {
            try {
                val deviceId = getDeviceId()
                val response = RetrofitInstance.api.createGuestAccount(
                    GuestLoginRequest(deviceId)
                )

                if (response.success) {
                    handleLoginSuccess(response.toLoginState())
                }

            } catch (e: Exception) {
                Log.e("AUTH", "Guest login 실패", e)
            }
        }
    }

    /* ------------------ LOGOUT ------------------ */
    fun logout() {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.logout()
            } catch (_: Exception) {
            } finally {
                RetrofitInstance.tokenDataStore.clearAccessToken()

                _signupCompleted.value = false
                _loginState.value = null
                _myInfo.value = null

                _logoutCompleted.value = true
                _withdrawCompleted.value = false
            }
        }
    }

    fun consumeLogoutCompleted() {
        _logoutCompleted.value = false
    }

    /* ------------------ WITHDRAW ------------------ */
    fun withdraw() {
        viewModelScope.launch {
            var isSuccess = false

            try {
                val response = RetrofitInstance.api.withdraw()
                if (response.success) isSuccess = true
            } catch (e: Exception) {
                Log.e("AUTH", "회원탈퇴 실패", e)
            }

            if (isSuccess) {
                UserApiClient.instance.unlink { }
            }

            RetrofitInstance.tokenDataStore.clearAccessToken()

            _signupCompleted.value = false
            _loginState.value = null
            _myInfo.value = null

            _withdrawCompleted.value = true
            _logoutCompleted.value = false
        }
    }

    fun consumeWithdrawCompleted() {
        _withdrawCompleted.value = false
    }

    /* ------------------ MY INFO ------------------ */
    fun fetchMyInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMyInfo()
                if (response.success) {
                    _myInfo.value = response.toMyInfo()
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _myInfo.value = null
                }
            }
        }
    }

    /* ------------------ COMMON LOGIN ------------------ */
    private fun handleLoginSuccess(loginState: LoginState) {
        viewModelScope.launch {
            RetrofitInstance.tokenDataStore.saveAccessToken(loginState.accessToken)
            _loginState.value = loginState
            fetchMyInfo()
        }
    }

    fun resetSignupState() {
        _signupCompleted.value = false
    }

    /* ------------------ DEVICE ID ------------------ */
    private fun getDeviceId(): String {
        val androidId = Settings.Secure.getString(
            getApplication<Application>().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return androidId ?: UUID.randomUUID().toString()
    }
}
