package com.example.aac.ui.features.auto_sentence

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.RoutineDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.aac.data.remote.dto.CreateRoutineRequest
import com.example.aac.data.remote.dto.RoutineUpdateRequest

data class AutoSentenceRoutineUiState(
    val isLoading: Boolean = false,
    val routines: List<RoutineDto> = emptyList(),
    val errorMessage: String? = null
)

class AutoSentenceRoutineViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AutoSentenceRoutineUiState())
    val uiState: StateFlow<AutoSentenceRoutineUiState> = _uiState

    fun createRoutine(
        request: CreateRoutineRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // (선택) 로딩 표시
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val res = RetrofitInstance.api.createRoutine(request)

                if (res.success) {
                    fetchRoutines()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message ?: "생성 실패"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "네트워크 오류"
                )
            }
        }
    }

    fun fetchRoutines() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val res = RetrofitInstance.api.getRoutines()
                if (res.success) {
                    val list = res.data?.routines.orEmpty()
                    _uiState.value = _uiState.value.copy(isLoading = false, routines = list)
                    Log.d("ROUTINE", "루틴 조회 성공: ${list.size}")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message ?: "루틴 조회 실패"
                    )
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "루틴 조회 예외", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "네트워크 오류"
                )
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
                val res = RetrofitInstance.api.updateRoutine(id = id, body = request)

                if (res.success) {
                    val updated = res.data?.routine

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        routines = if (updated != null) {
                            _uiState.value.routines.map { if (it.id == updated.id) updated else it }
                        } else {
                            _uiState.value.routines
                        }
                    )

                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message ?: "수정 실패"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "네트워크 오류"
                )
            }
        }
    }

    fun deleteRoutine(
        id: String,
        onSuccess: () -> Unit = {}
    ) {
        deleteRoutines(ids = listOf(id), onSuccess = onSuccess)
    }

    fun deleteRoutines(
        ids: List<String>,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val res = RetrofitInstance.api.deleteRoutines(
                    body = com.example.aac.data.remote.dto.DeleteRoutinesRequest(ids = ids)
                )

                if (res.success) {
                    fetchRoutines()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message ?: "삭제 실패"
                    )
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "루틴 선택 삭제 예외", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "네트워크 오류"
                )
            }
        }
    }

    fun deleteAllRoutines(
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val res = RetrofitInstance.api.deleteAllRoutines()

                if (res.success) {
                    fetchRoutines()
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message ?: "삭제 실패"
                    )
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "루틴 전체 삭제 예외", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "네트워크 오류"
                )
            }
        }
    }





}
