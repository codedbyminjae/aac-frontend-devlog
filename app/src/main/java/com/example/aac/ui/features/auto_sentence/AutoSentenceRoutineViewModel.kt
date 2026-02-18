package com.example.aac.ui.features.auto_sentence

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AutoSentenceRoutineUiState(
    val isLoading: Boolean = false,
    val routines: List<RoutineDto> = emptyList(),
    val errorMessage: String? = null
)

class AutoSentenceRoutineViewModel : ViewModel() {

    // ----------------------------------------------------
    // UI State
    // ----------------------------------------------------
    private val _uiState = MutableStateFlow(AutoSentenceRoutineUiState())
    val uiState: StateFlow<AutoSentenceRoutineUiState> = _uiState

    // ----------------------------------------------------
    // Modal State
    // ----------------------------------------------------
    private val _modalRoutine = MutableStateFlow<RoutineDto?>(null)
    val modalRoutine: StateFlow<RoutineDto?> = _modalRoutine

    // 현재 표시된 모달 ID (중복 방지)
    private var currentModalId: String? = null

    // ----------------------------------------------------
    // CRUD
    // ----------------------------------------------------

    fun createRoutine(request: CreateRoutineRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val res = RetrofitInstance.api.createRoutine(request)
                if (res.success) {
                    fetchRoutines()
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = res.message ?: "생성 실패"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "네트워크 오류")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun fetchRoutines() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val res = RetrofitInstance.api.getRoutines()
                if (res.success) {
                    _uiState.value = _uiState.value.copy(
                        routines = res.data?.routines.orEmpty()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = res.message ?: "루틴 조회 실패"
                    )
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "루틴 조회 예외", e)
                _uiState.value = _uiState.value.copy(errorMessage = "네트워크 오류")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun updateRoutine(
        id: String,
        request: RoutineUpdateRequest,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val res = RetrofitInstance.api.updateRoutine(id, request)
                if (res.success) {
                    val updated = res.data?.routine
                    _uiState.value = _uiState.value.copy(
                        routines = _uiState.value.routines.map {
                            if (it.id == updated?.id) updated else it
                        }
                    )
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = res.message ?: "수정 실패"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "네트워크 오류")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun deleteRoutine(id: String, onSuccess: () -> Unit = {}) {
        deleteRoutines(listOf(id), onSuccess)
    }

    fun deleteRoutines(ids: List<String>, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val res = RetrofitInstance.api.deleteRoutines(DeleteRoutinesRequest(ids))
                if (res.success) {
                    fetchRoutines()
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = res.message ?: "삭제 실패"
                    )
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "삭제 예외", e)
                _uiState.value = _uiState.value.copy(errorMessage = "네트워크 오류")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun deleteAllRoutines(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val res = RetrofitInstance.api.deleteAllRoutines()
                if (res.success) {
                    fetchRoutines()
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = res.message ?: "삭제 실패"
                    )
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "전체 삭제 예외", e)
                _uiState.value = _uiState.value.copy(errorMessage = "네트워크 오류")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // ----------------------------------------------------
    // Modal (Polling 기반)
    // ----------------------------------------------------

    fun checkRoutineModal() {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.getRoutineModal()
                if (res.success) {
                    val routine = res.data?.routine
                    Log.d("MODAL", "서버 응답 id = ${routine?.id}")

                    // 같은 루틴은 다시 표시하지 않음
                    if (routine != null && routine.id != currentModalId) {
                        currentModalId = routine.id
                        _modalRoutine.value = routine
                    }
                }
            } catch (e: Exception) {
                Log.e("MODAL", "checkRoutineModal 실패", e)
            }
        }
    }

    fun snoozeRoutine(id: String) {
        clearModal() // UI 즉시 닫기
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.snoozeRoutineModal(id)
                if (!res.success) {
                    Log.e("MODAL", "snooze 실패 응답")
                }
            } catch (e: Exception) {
                Log.e("MODAL", "snooze 네트워크 실패", e)
            }
        }
    }

    fun dismissRoutine(id: String) {
        clearModal() // UI 즉시 닫기
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.dismissRoutineModal(id)
                if (!res.success) {
                    Log.e("MODAL", "dismiss 실패 응답")
                }
            } catch (e: Exception) {
                Log.e("MODAL", "dismiss 네트워크 실패", e)
            }
        }
    }

    fun clearModal() {
        _modalRoutine.value = null
        currentModalId = null
    }
}
