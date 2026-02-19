package com.example.aac.ui.features.auto_sentence

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

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
    // TTS Player (MP3)
    // ----------------------------------------------------
    private var mediaPlayer: MediaPlayer? = null
    private var currentMp3File: File? = null

    // TTS 요청이 연타로 겹치는 것 방지용 (선택이지만 안정성↑)
    @Volatile
    private var isTtsLoading: Boolean = false

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
                    _uiState.value = _uiState.value.copy(errorMessage = res.message ?: "생성 실패")
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
                    _uiState.value = _uiState.value.copy(routines = res.data?.routines.orEmpty())
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = res.message ?: "루틴 조회 실패")
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
                        routines = _uiState.value.routines.map { if (it.id == updated?.id) updated else it }
                    )
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = res.message ?: "수정 실패")
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
                    _uiState.value = _uiState.value.copy(errorMessage = res.message ?: "삭제 실패")
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
                    _uiState.value = _uiState.value.copy(errorMessage = res.message ?: "삭제 실패")
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

                    if (routine == null) {
                        Log.d("MODAL", "🟡 getRoutineModal: routine = null")
                        return@launch
                    }

                    Log.d("MODAL", "🟢 getRoutineModal: id=${routine.id}, snoozedUntil=${routine.snoozedUntil}")

                    // 같은 루틴은 다시 표시하지 않음
                    if (routine.id != currentModalId) {
                        currentModalId = routine.id
                        _modalRoutine.value = routine
                        Log.d("MODAL", "🔥 모달 표시: id=${routine.id}")
                    } else {
                        Log.d("MODAL", "⚪ 모달 스킵(중복): currentModalId=$currentModalId")
                    }
                } else {
                    Log.e("MODAL", "getRoutineModal 실패: ${res.message}")
                }
            } catch (e: Exception) {
                Log.e("MODAL", "checkRoutineModal 실패", e)
            }
        }
    }

    fun snoozeRoutine(id: String) {
        clearModal()         // UI 즉시 닫기
        stopTtsInternal()    // 모달 액션 시 현재 재생도 끊기 (원하는 UX)

        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.snoozeRoutineModal(id, SnoozeRequest(minutes = 5))
                if (res.success) {
                    val routine = res.data?.routine
                    Log.d("MODAL", "✅ snooze 성공, routine.snoozedUntil = ${routine?.snoozedUntil}")
                    Log.d("MODAL", "✅ snooze 성공, message = ${routine?.message}, id=${routine?.id}")
                } else {
                    Log.e("MODAL", "snooze 실패 응답: ${res.message}")
                }
            } catch (e: Exception) {
                Log.e("MODAL", "snooze 네트워크 실패", e)
            }
        }
    }

    fun dismissRoutine(id: String) {
        clearModal()         // UI 즉시 닫기
        stopTtsInternal()    // 모달 액션 시 현재 재생도 끊기 (원하는 UX)

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

    // ----------------------------------------------------
    // TTS (서버 MP3) : 서버 호출 -> 파일 저장 -> MediaPlayer 재생
    // ----------------------------------------------------
    fun playRoutineTts(
        context: Context,
        text: String,
        voiceKey: String? = null // 필요하면 AppNavGraph의 voiceSettingId를 넘겨서 사용
    ) {
        if (text.isBlank()) return

        // 연타 방지 (서버/파일/MediaPlayer 중복 생성 방지)
        if (isTtsLoading) {
            Log.d("TTS", "⏳ already loading... skip")
            return
        }
        isTtsLoading = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("TTS", "▶️ request mp3: text='$text', voiceKey=$voiceKey")

                // 1) 서버 호출 (MP3 바이너리)
                val res = RetrofitInstance.api.requestTtsMp3(
                    TtsRequest(text = text, voiceKey = voiceKey)
                )

                if (!res.isSuccessful) {
                    Log.e("TTS", "❌ TTS HTTP 실패: code=${res.code()}")
                    return@launch
                }

                val body = res.body()
                if (body == null) {
                    Log.e("TTS", "❌ TTS 응답 body=null")
                    return@launch
                }

                // 2) 임시 파일로 저장 (cache)
                val appCtx = context.applicationContext
                val outFile = File(appCtx.cacheDir, "tts_${UUID.randomUUID()}.mp3")

                // ResponseBody까지 확실히 close
                body.use { responseBody ->
                    responseBody.byteStream().use { input ->
                        FileOutputStream(outFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                // 이전 파일 정리
                currentMp3File?.let { runCatching { it.delete() } }
                currentMp3File = outFile

                Log.d("TTS", "✅ mp3 saved: ${outFile.absolutePath} (${outFile.length()} bytes)")

                // 3) MediaPlayer 재생은 Main에서
                withContext(Dispatchers.Main) {
                    stopTtsInternal() // 현재 재생 중이면 정지/해제

                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(outFile.absolutePath)

                        setOnPreparedListener {
                            Log.d("TTS", "✅ prepared -> start")
                            start()
                        }

                        setOnCompletionListener {
                            Log.d("TTS", "✅ complete -> release")
                            stopTtsInternal()
                        }

                        setOnErrorListener { _, what, extra ->
                            Log.e("TTS", "❌ MediaPlayer error what=$what extra=$extra")
                            stopTtsInternal()
                            true
                        }

                        prepareAsync() // 비동기 준비
                    }
                }
            } catch (e: Exception) {
                Log.e("TTS", "❌ playRoutineTts 실패", e)
            } finally {
                isTtsLoading = false
            }
        }
    }

    // MediaPlayer 정리
    private fun stopTtsInternal() {
        runCatching { mediaPlayer?.stop() }
        runCatching { mediaPlayer?.release() }
        mediaPlayer = null
    }

    // ViewModel 종료 시 리소스 정리
    override fun onCleared() {
        super.onCleared()
        stopTtsInternal()
        currentMp3File?.let { runCatching { it.delete() } }
        currentMp3File = null
    }
}
