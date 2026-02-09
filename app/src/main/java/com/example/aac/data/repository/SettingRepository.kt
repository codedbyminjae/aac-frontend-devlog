package com.example.aac.data.repository

import android.util.Log
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.GridSettingRequest

class SettingRepository {

    suspend fun getGridColumns(): Int? {
        return try {
            val response = RetrofitInstance.api.getGridSetting()
            if (response.success) {
                response.data.gridColumns
            } else {
                Log.e("SettingRepository", "조회 실패: ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("SettingRepository", "네트워크 에러: ${e.message}")
            null
        }
    }

    suspend fun updateGridColumns(columns: Int): Boolean {
        return try {
            val request = GridSettingRequest(gridColumns = columns)
            val response = RetrofitInstance.api.updateGridSetting(request)
            if (response.success) {
                Log.d("SettingRepository", "수정 성공: $columns")
                true
            } else {
                Log.e("SettingRepository", "수정 실패: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Log.e("SettingRepository", "네트워크 에러: ${e.message}")
            false
        }
    }
}