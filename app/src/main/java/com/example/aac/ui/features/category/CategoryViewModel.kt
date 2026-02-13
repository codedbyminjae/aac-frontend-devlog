package com.example.aac.ui.features.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aac.data.mapper.IconMapper
import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.repository.CategoryRepositoryImpl
import com.example.aac.domain.model.Category
import com.example.aac.domain.model.Word
import com.example.aac.domain.repository.CategoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _wordCards = MutableStateFlow<List<Word>>(emptyList())
    val wordCards = _wordCards.asStateFlow()

    private val _selectedWordCategoryId = MutableStateFlow<String?>(null)
    val selectedWordCategoryId = _selectedWordCategoryId.asStateFlow()

    sealed class UiEvent {
        object SaveCompleted : UiEvent()
        data class Error(val message: String) : UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchCategories()
        fetchWords(null)
    }

    // ✅ 카테고리 전체 조회
    fun fetchCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onSuccess { list ->
                    val sortedList = list.sortedBy { it.displayOrder }
                    _categories.value = sortedList
                    Log.d("TAG_CHECK", "📥 [카테고리 조회 성공] 총 ${sortedList.size}개")
                }
                .onFailure {
                    Log.e("TAG_CHECK", "❌ 카테고리 조회 실패: ${it.message}")
                }
        }
    }

    // ✅ 낱말 조회
    fun fetchWords(categoryId: String?) {
        _selectedWordCategoryId.value = categoryId
        viewModelScope.launch {
            repository.getWords(categoryId)
                .onSuccess { list -> _wordCards.value = list }
        }
    }

    // ✅ [신규 추가] 단일 카테고리 생성
    fun createCategory(name: String, iconRes: Int) {
        viewModelScope.launch {
            val keyString = IconMapper.toRemoteKey(iconRes)
            Log.d("TAG_CHECK", "🚀 [카테고리 생성 시도] 이름: $name, 아이콘키: $keyString")

            repository.createCategory(name, keyString)
                .onSuccess {
                    Log.d("TAG_CHECK", "✅ 카테고리 생성 성공")
                    fetchCategories()
                }
                .onFailure {
                    Log.e("TAG_CHECK", "❌ 카테고리 생성 실패: ${it.message}")
                    _eventFlow.emit(UiEvent.Error("생성에 실패했습니다."))
                }
        }
    }

    // 🔥 [로그 추가됨] 단일 카테고리 수정
    fun updateCategory(id: String, newName: String, newIconRes: Int) {
        viewModelScope.launch {
            // 1. 현재 리스트에서 순서(displayOrder) 가져오기
            val currentCategory = _categories.value.find { it.id == id }
            val currentOrder = currentCategory?.displayOrder ?: 0

            // 2. 아이콘 리소스 ID를 서버용 키값(String)으로 변환
            val keyString = IconMapper.toRemoteKey(newIconRes)

            // 🔍 [디버깅 로그] 서버로 보내기 직전 데이터 확인
            Log.d("UPDATE_DEBUG", "========================================")
            Log.d("UPDATE_DEBUG", "🔄 [수정 요청 시작]")
            Log.d("UPDATE_DEBUG", "   🆔 ID: $id")
            Log.d("UPDATE_DEBUG", "   📝 이름: $newName")
            Log.d("UPDATE_DEBUG", "   🔑 아이콘Key: $keyString (원본Res: $newIconRes)")
            Log.d("UPDATE_DEBUG", "   🔢 순서(displayOrder): $currentOrder (0이면 순서 초기화 주의!)")
            Log.d("UPDATE_DEBUG", "========================================")

            repository.updateCategory(
                id = id,
                name = newName,
                iconKey = keyString,
                displayOrder = currentOrder
            ).onSuccess {
                Log.d("UPDATE_DEBUG", "✅ [수정 성공] 서버 응답 OK!")
                Log.d("UPDATE_DEBUG", "   👉 반환된 데이터: ${it.name}, ${it.iconKey}")
                fetchCategories() // 목록 새로고침
            }.onFailure {
                Log.e("UPDATE_DEBUG", "❌ [수정 실패] 에러 메시지: ${it.message}")
                it.printStackTrace() // 상세 에러 스택 출력
                _eventFlow.emit(UiEvent.Error("수정에 실패했습니다."))
            }
        }
    }

    // ✅ 카테고리 삭제
    fun deleteCategory(id: String) {
        viewModelScope.launch {
            Log.d("TAG_CHECK", "🗑️ [카테고리 삭제 시도] ID: $id")
            repository.deleteCategory(id).onSuccess {
                Log.d("TAG_CHECK", "✅ 삭제 성공")
                fetchCategories()
            }.onFailure {
                Log.e("TAG_CHECK", "❌ 삭제 실패: ${it.message}")
            }
        }
    }

    // ✅ 일괄 저장 (순서 변경 및 신규 생성 처리)
    fun saveCategoryList(editedList: List<CategoryEditData>) {
        viewModelScope.launch {
            Log.d("ORDER_DEBUG", "💾 [일괄 저장 시작] UI에서 넘어온 리스트 순서:")
            editedList.forEachIndexed { index, item ->
                Log.d("ORDER_DEBUG", "   [$index] ${item.title} (ID: ${item.id})")
            }

            val deferredJobs = editedList.mapIndexed { index, item ->
                async {
                    if (item.id == null) {
                        val keyString = IconMapper.toRemoteKey(item.iconRes)
                        val result = repository.createCategory(item.title, keyString)
                        val created = result.getOrNull()
                        if (created != null) created.id to index else null
                    } else {
                        item.id to index
                    }
                }
            }

            val results = deferredJobs.awaitAll()
            val finalOrderMap = results.filterNotNull().toMap()

            Log.d("ORDER_DEBUG", "📦 [순서 변경 API 전송 데이터]: $finalOrderMap")

            if (finalOrderMap.isNotEmpty()) {
                repository.updateCategoryOrders(finalOrderMap)
                    .onSuccess {
                        Log.d("ORDER_DEBUG", "✅ 순서 변경 API 성공")
                    }
                    .onFailure {
                        Log.e("ORDER_DEBUG", "❌ 순서 변경 API 실패: ${it.message}")
                        _eventFlow.emit(UiEvent.Error("순서 저장 실패: ${it.message}"))
                    }
            }

            delay(300)
            fetchCategories()
            Log.d("TAG_CHECK", "🎉 저장 프로세스 완료, 이벤트 발생")
            _eventFlow.emit(UiEvent.SaveCompleted)
        }
    }
}

// 팩토리 클래스
class CategoryViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            val api = RetrofitInstance.api
            val repository = CategoryRepositoryImpl(api)
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}