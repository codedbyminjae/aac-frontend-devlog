package com.example.aac.ui.features.speak_setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.repository.SettingRepository // ✅ 추가
import com.example.aac.data.repository.WordRepository
import com.example.aac.domain.model.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpeakSettingViewModel : ViewModel() {

    private val wordRepository = WordRepository()
    private val settingRepository = SettingRepository()

    // 단어 리스트 상태
    private val _uiState = MutableStateFlow<List<Word>>(emptyList())
    val uiState: StateFlow<List<Word>> = _uiState.asStateFlow()

    private val _serverGridColumns = MutableStateFlow<Int>(7)
    val serverGridColumns: StateFlow<Int> = _serverGridColumns.asStateFlow()

    init {
        fetchWords()
        fetchGridSetting()
    }

    private fun fetchWords() {
        viewModelScope.launch {
            val words = wordRepository.getWords()
            _uiState.value = words
        }
    }

    private fun fetchGridSetting() {
        viewModelScope.launch {
            val columns = settingRepository.getGridColumns()
            if (columns != null) {
                _serverGridColumns.value = columns
            }
        }
    }

    fun saveGridSetting(columns: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            val isSuccess = settingRepository.updateGridColumns(columns)
            if (isSuccess) {
                _serverGridColumns.value = columns
                onComplete()
            }
        }
    }
}