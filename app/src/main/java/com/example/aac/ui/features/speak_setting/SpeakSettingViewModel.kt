package com.example.aac.ui.features.speak_setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.domain.model.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpeakSettingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<List<Word>>(emptyList())
    val uiState: StateFlow<List<Word>> = _uiState.asStateFlow()

    init {
        fetchWords()
    }

    private fun fetchWords() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWords()

                if (response.success) {
                    _uiState.value = response.data.words
                    Log.d("SpeakSettingViewModel", "데이터 로드 성공: ${response.data.words.size}개")
                } else {
                    Log.e("SpeakSettingViewModel", "데이터 로드 실패: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("SpeakSettingViewModel", "에러 발생: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}