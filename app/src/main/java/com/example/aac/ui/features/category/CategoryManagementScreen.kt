package com.example.aac.ui.features.category

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aac.R
import com.example.aac.data.mapper.IconMapper
import com.example.aac.ui.components.CommonSaveDialog
import com.example.aac.ui.components.CustomTopBar
import com.example.aac.ui.features.category.components.ManagementTabRow
import com.example.aac.ui.features.category.components.WordCardManagementContent
import com.example.aac.ui.features.category.CategoryManagementContent

@Composable
fun CategoryManagementScreen(
    onBackClick: () -> Unit = {},
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModelFactory())
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showSaveDialog by remember { mutableStateOf(false) }

    // 1. 서버 데이터 관찰
    val serverCategories by viewModel.categories.collectAsState()
    val serverWords by viewModel.wordCards.collectAsState()
    val selectedWordCategoryId by viewModel.selectedWordCategoryId.collectAsState()

    // 2. 로컬 상태 (UI에서 편집 중인 리스트)
    val categoryList = remember { mutableStateListOf<CategoryEditData>() }

    // 🔥 서버 데이터 동기화 및 iconKey 로그 확인
    LaunchedEffect(serverCategories) {
        Log.d("ICON_KEY_CHECK", "====================================")
        Log.d("ICON_KEY_CHECK", "📡 서버 응답 카테고리 수: ${serverCategories.size}개")
        serverCategories.forEach { category ->
            Log.d("ICON_KEY_CHECK", "이름: ${category.name} | ID: ${category.id} | 🔑 iconKey(String): ${category.iconKey}")
        }
        Log.d("ICON_KEY_CHECK", "====================================")

        if (categoryList.isEmpty() || categoryList.size != serverCategories.size) {
            categoryList.clear()
            categoryList.addAll(
                serverCategories.map { category ->
                    CategoryEditData(
                        id = category.id,
                        title = category.name,
                        count = 0,
                        iconRes = IconMapper.toLocalResource(category.iconKey)
                    )
                }
            )
        }
    }

    // 3. 변경 감지 (서버 데이터와 로컬 리스트 비교)
    val hasCategoryChanges by remember {
        derivedStateOf {
            if (categoryList.size != serverCategories.size) return@derivedStateOf true
            val serverData = serverCategories.map {
                Triple(it.id, it.name, IconMapper.toLocalResource(it.iconKey))
            }
            val localData = categoryList.map {
                Triple(it.id, it.title, it.iconRes)
            }
            serverData != localData
        }
    }

    val hasWordChanges = false
    val hasChanges = hasCategoryChanges || hasWordChanges

    // 4. 이벤트 처리 (저장 완료 시 화면 종료)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is CategoryViewModel.UiEvent.SaveCompleted -> {
                    Log.d("TAG_CHECK", "🚀 저장 완료! 설정 화면으로 이동합니다.")
                    onBackClick()
                }
                is CategoryViewModel.UiEvent.Error -> {
                    Log.e("TAG_CHECK", "❌ 저장 실패: ${event.message}")
                }
            }
        }
    }

    // 시스템 뒤로가기 버튼 처리
    BackHandler { if (hasChanges) showSaveDialog = true else onBackClick() }

    // 5. UI 구성
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        CustomTopBar(
            title = if (selectedTabIndex == 0) "카테고리 관리" else "낱말 카드 관리",
            onBackClick = { if (hasChanges) showSaveDialog = true else onBackClick() },
            actionText = "저장하기",
            onActionClick = {
                if (selectedTabIndex == 0) {
                    if (hasCategoryChanges) {
                        viewModel.saveCategoryList(categoryList)
                    } else {
                        onBackClick()
                    }
                }
            }
        )

        ManagementTabRow(selectedTabIndex, { selectedTabIndex = it })

        if (selectedTabIndex == 0) {
            CategoryManagementContent(
                categoryList = categoryList,
                onAddCategory = { newName, newIcon ->
                    categoryList.add(
                        CategoryEditData(
                            id = null,
                            title = newName,
                            iconRes = newIcon,
                            count = 0
                        )
                    )
                },

                // 🔥 [수정 완료] 이제 뷰모델을 호출해서 서버에 저장 요청을 보냅니다!
                onEditCategory = { targetId, newName, newIcon ->
                    // 1. 서버 API 호출 (즉시 저장)
                    Log.d("TAG_CHECK", "✏️ UI에서 편집 요청: $newName")
                    viewModel.updateCategory(targetId, newName, newIcon)

                    // 2. 로컬 리스트 업데이트 (UI 즉시 반영)
                    val index = categoryList.indexOfFirst { it.id == targetId }
                    if (index != -1) {
                        categoryList[index] = categoryList[index].copy(
                            title = newName,
                            iconRes = newIcon
                        )
                    }
                },

                onDeleteCategory = { targetId ->
                    val item = categoryList.find { it.id == targetId }
                    if (item != null) {
                        categoryList.remove(item)
                        if (targetId != null) {
                            Log.d("TAG_CHECK", "🗑️ 삭제 요청: $targetId")
                            viewModel.deleteCategory(targetId)
                        }
                    }
                }
            )
        } else {
            WordCardManagementContent(
                categories = serverCategories,
                wordList = serverWords,
                selectedCategoryId = selectedWordCategoryId,
                onCategorySelect = { viewModel.fetchWords(it) }
            )
        }
    }

    // 저장 확인 팝업
    if (showSaveDialog) {
        CommonSaveDialog(
            message = "변경사항을\n저장하시겠어요?",
            onDismiss = { showSaveDialog = false },
            onSave = {
                if (selectedTabIndex == 0 && hasCategoryChanges) {
                    viewModel.saveCategoryList(categoryList)
                } else {
                    onBackClick()
                }
                showSaveDialog = false
            }
        )
    }
}