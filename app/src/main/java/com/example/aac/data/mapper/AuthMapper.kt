package com.example.aac.data.mapper

import com.example.aac.data.remote.dto.GuestLoginResponse
import com.example.aac.data.remote.dto.KakaoLoginResponse
import com.example.aac.domain.model.LoginState

/* ------------------ 게스트 로그인 매핑 ------------------ */

fun GuestLoginResponse.toLoginState(): LoginState {
    val data = requireNotNull(this.data) {
        "GuestLoginResponse.data is null"
    }

    return LoginState(
        userId = data.user.id,
        nickname = data.user.nickname,
        accountType = data.user.accountType,
        accessToken = data.tokens.accessToken,
        tokenType = data.tokens.tokenType,
        expiresIn = data.tokens.expiresIn
    )
}

/* ------------------ 카카오 로그인 매핑 ------------------ */

fun KakaoLoginResponse.toLoginState(): LoginState {
    val data = requireNotNull(this.data) {
        "KakaoLoginResponse.data is null"
    }

    val user = requireNotNull(data.user) {
        "KakaoLoginResponse.user is null (신규회원 케이스에서 호출됨)"
    }

    val tokens = requireNotNull(data.tokens) {
        "KakaoLoginResponse.tokens is null (신규회원 케이스에서 호출됨)"
    }

    return LoginState(
        userId = user.id,
        nickname = user.nickname,
        accountType = user.accountType,
        accessToken = tokens.accessToken,
        tokenType = tokens.tokenType,
        expiresIn = tokens.expiresIn
    )
}

