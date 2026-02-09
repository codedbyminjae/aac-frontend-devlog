package com.example.aac.data.local

import kotlinx.coroutines.runBlocking

/**
 * AuthInterceptor에서 사용할 토큰 조회용 Provider
 * - Interceptor는 suspend 사용 불가
 * - runBlocking으로 DataStore 값을 동기적으로 가져온다
 */
class TokenProvider(
    private val tokenDataStore: TokenDataStore
) {

    fun getAccessToken(): String? = runBlocking {
        tokenDataStore.getAccessToken()
    }
}
