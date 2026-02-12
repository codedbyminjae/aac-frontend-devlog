package com.example.aac.ui.features.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aac.data.mapper.IconMapper // ✅ Mapper Import
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
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchCategories()
        fetchWords(null)
    }

    // ✅ 카테고리 조회
    fun fetchCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onSuccess { list ->
                    val sortedList = list.sortedBy { it.displayOrder }
                    _categories.value = sortedList
                    Log.d("TAG_CHECK", "📥 [카테고리 조회] : ${sortedList.size}개 로드됨")
                    // ✅ 로그에서 iconKey가 잘 들어왔는지 확인
                    list.forEach {
                        Log.d("TAG_CHECK", "   - ${it.name} : ${it.iconKey}")
                    }
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

    // ✅ 카테고리 저장 (생성 + 순서)
    fun saveCategoryList(editedList: List<CategoryEditData>) {
        viewModelScope.launch {
            Log.d("TAG_CHECK", "💾 [저장 시작]")

            val deferredJobs = editedList.mapIndexed { index, item ->
                async {
                    if (item.id == null) {
                        // 🟢 [핵심] Int -> String 변환 (저장)
                        val keyString = IconMapper.toRemoteKey(item.iconRes)
                        Log.d("TAG_CHECK", "   🆕 신규생성: ${item.title} -> $keyString")

                        val result = repository.createCategory(item.title, keyString)
                        val created = result.getOrNull()
                        if (created != null) created.id to index else null
                    } else {
                        item.id to index
                    }
                }
            }

            val finalOrderMap = deferredJobs.awaitAll().filterNotNull().toMap()

            if (finalOrderMap.isNotEmpty()) {
                repository.updateCategoryOrders(finalOrderMap)
            }

            delay(300)
            fetchCategories()
            _eventFlow.emit(UiEvent.SaveCompleted)
        }
    }

    // ✅ 단일 수정
    fun updateCategory(id: String, newName: String, newIconRes: Int) {
        viewModelScope.launch {
            val keyString = IconMapper.toRemoteKey(newIconRes)
            repository.updateCategory(id, newName, keyString)
                .onSuccess { fetchCategories() }
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            repository.deleteCategory(id).onSuccess { fetchCategories() }
        }
    }
}

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