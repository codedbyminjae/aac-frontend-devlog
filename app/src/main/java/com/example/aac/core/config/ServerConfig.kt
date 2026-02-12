package com.example.aac.core.config

import com.example.aac.BuildConfig

object ServerConfig {
    const val KAKAO_LOGIN_PATH = "/api/auth/kakao"

    val BASE_URL: String
        get() = BuildConfig.BASE_URL
}
