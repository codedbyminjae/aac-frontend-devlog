package com.example.aac.ui.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aac.R
import com.example.aac.data.remote.dto.CategoryResponseItem
import com.example.aac.data.remote.dto.MainWordItem
import com.example.aac.data.repository.MainRepository
import com.example.aac.ui.components.CategoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    private val _categories = MutableStateFlow<List<CategoryItem>>(emptyList())
    val categories: StateFlow<List<CategoryItem>> = _categories.asStateFlow()

    private val _words = MutableStateFlow<List<MainWordItem>>(emptyList())
    val words: StateFlow<List<MainWordItem>> = _words.asStateFlow()

    private val _selectedCategoryIndex = MutableStateFlow(0)
    val selectedCategoryIndex: StateFlow<Int> = _selectedCategoryIndex.asStateFlow()

    private val _selectedCards = MutableStateFlow<List<MainWordItem>>(emptyList())
    val selectedCards: StateFlow<List<MainWordItem>> = _selectedCards.asStateFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            val fixedCategories = listOf(
                CategoryItem(name = "전체", iconRes = R.drawable.ic_default, isSelected = true, serverId = null),
                CategoryItem(name = "즐겨찾기", iconRes = R.drawable.ic_favorite, isSelected = false, serverId = null)
            )

            val fetchedCategories = repository.getCategories()

            val serverCategories = fetchedCategories.map { item: CategoryResponseItem ->
                val icon = when (item.name) {
                    "사람" -> R.drawable.ic_human
                    "행동" -> R.drawable.ic_act
                    "감정" -> R.drawable.ic_emotion
                    "음식" -> R.drawable.ic_food
                    "장소" -> R.drawable.ic_place
                    "신체" -> R.drawable.ic_human
                    "어미" -> R.drawable.ic_question
                    else -> R.drawable.ic_default
                }

                CategoryItem(
                    name = item.name,
                    iconRes = icon,
                    isSelected = false,
                    serverId = item.categoryId
                )
            }

            _categories.value = fixedCategories + serverCategories
            selectCategory(0)
        }
    }

    fun selectCategory(index: Int) {
        _selectedCategoryIndex.value = index
        val currentList = _categories.value

        // UI 선택 상태 갱신
        _categories.value = currentList.mapIndexed { i, item ->
            item.copy(isSelected = i == index)
        }

        val selectedItem = currentList.getOrNull(index) ?: return

        // 단어 목록 갱신
        viewModelScope.launch {
            val fetchedWords = when (index) {
                0 -> repository.getWords()
                1 -> repository.getWords(onlyFavorite = true)
                else -> {
                    val catId = selectedItem.serverId
                    // catId가 null이면 빈 리스트, 있으면 조회
                    if (catId != null) repository.getWords(categoryId = catId) else emptyList()
                }
            }
            _words.value = fetchedWords
        }
    }

    fun addCard(card: MainWordItem) {
        _selectedCards.value = _selectedCards.value + card
    }

    fun removeCard(index: Int) {
        val currentList = _selectedCards.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _selectedCards.value = currentList
        }
    }

    fun clearSelectedCards() {
        _selectedCards.value = emptyList()
    }

}