package com.example.aac.ui.features.speak_setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.api.RetrofitInstance
import com.example.aac.data.model.SpeakSettingCardUiModel
import com.example.aac.data.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpeakSettingUiState(
    val selectedColumnCount: Int = 7,
    val wordList: List<SpeakSettingCardUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val initialColumnCount: Int = 7
)

class SpeakSettingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SpeakSettingUiState())
    val uiState: StateFlow<SpeakSettingUiState> = _uiState.asStateFlow()

    init {
        fetchWords()
    }

    private fun fetchWords() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val response = RetrofitInstance.api.getWords()

                if (response.success) {
                    val uiWords = response.data.words.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            wordList = uiWords,
                            isLoading = false
                        )
                    }
                    Log.d("API_SUCCESS", "불러온 단어 개수: ${uiWords.size}")
                } else {
                    Log.e("API_ERROR", "실패 메시지: ${response.message}")
                    _uiState.update { it.copy(isLoading = false) }
                }

            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "에러 발생: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateColumnCount(count: Int) {
        _uiState.update { it.copy(selectedColumnCount = count) }
    }

    fun saveSettings() {
        val currentCount = _uiState.value.selectedColumnCount
        _uiState.update { it.copy(initialColumnCount = currentCount) }
    }

    fun hasChanges(): Boolean {
        return _uiState.value.selectedColumnCount != _uiState.value.initialColumnCount
    }
}