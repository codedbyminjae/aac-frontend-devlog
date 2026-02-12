package com.example.aac.data.repository

import android.util.Log
import com.example.aac.data.mapper.WordMapper
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.domain.model.Word

class WordRepository {

    suspend fun getWords(): List<Word> {
        return try {
            val response = RetrofitInstance.api.getWords()

            if (response.success) {
                // Mapper를 써서 변환 후 반환
                WordMapper.mapToDomain(response)
            } else {
                Log.e("WordRepository", "서버 에러: ${response.message}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("WordRepository", "네트워크 에러: ${e.message}")
            emptyList()
        }
    }
}