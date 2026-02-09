package com.example.aac.data.local

import kotlinx.coroutines.runBlocking

class TokenProvider(
    private val tokenDataStore: TokenDataStore
) {
    fun getAccessToken(): String? = runBlocking {
        tokenDataStore.getAccessToken()
    }
}