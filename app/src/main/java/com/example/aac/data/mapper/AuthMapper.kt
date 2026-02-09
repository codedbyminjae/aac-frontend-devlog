package com.example.aac.data.mapper

import com.example.aac.data.remote.dto.GuestLoginResponse
import com.example.aac.domain.model.LoginState

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
