package com.example.aac.ui.features.category

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aac.R
import com.example.aac.ui.components.CommonDeleteDialog
import com.example.aac.ui.features.category.components.*
import sh.calvin.reorderable.*

@Composable
fun CategoryManagementContent(
    categoryList: SnapshotStateList<CategoryEditData>,
    onAddCategory: (String, Int) -> Unit,        // 생성 API 연결용
    onEditCategory: (String, String, Int) -> Unit, // 수정 API 연결용
    onDeleteCategory: (String) -> Unit           // 삭제 API 연결용
) {

    LaunchedEffect(categoryList.toList()) { // 스냅샷 리스트를 일반 리스트로 변환해 관찰
        Log.d("DATA_CHECK", "====================================")
        Log.d("DATA_CHECK", "📊 현재 카테고리 리스트 아이템 수: ${categoryList.size}")
        categoryList.forEachIndexed { index, item ->
            Log.d("DATA_CHECK", "[$index] ID: ${item.id} | 이름: ${item.title} | 아이콘Res: ${item.iconRes} | 낱말수: ${item.count}")
        }
        Log.d("DATA_CHECK", "====================================")
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf<CategoryEditData?>(null) }

    val listState = rememberLazyListState()

    // [리스트 순서 변경 로직]
    val reorderableState = rememberReorderableLazyListState(listState) { from, to ->
        val fromKey = from.key
        val toKey = to.key
        val fromIndex = categoryList.indexOfFirst { (it.id ?: it.hashCode()) == fromKey }
        val toIndex = categoryList.indexOfFirst { (it.id ?: it.hashCode()) == toKey }

        if (fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
            categoryList.apply { add(toIndex, removeAt(fromIndex)) }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. 팁 박스
        item { TipBox() }

        // 2. 카테고리 추가 버튼
        item {
            Spacer(modifier = Modifier.height(8.dp))
            AddCategoryButton(onClick = { showAddDialog = true })
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 3. 카테고리 리스트 아이템들
        items(items = categoryList, key = { it.id ?: it.hashCode() }) { item ->
            ReorderableItem(state = reorderableState, key = item.id ?: item.hashCode()) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "elevation")

                CategoryEditListItem(
                    data = item,
                    isDragging = isDragging,
                    dragModifier = Modifier.draggableHandle(),
                    onEditClick = {
                        selectedCategory = item
                        showEditDialog = true
                    },
                    onDeleteClick = {
                        selectedCategory = item
                        showDeleteDialog = true
                    }
                )
            }
        }
    }

    // ==========================================
    // 다이얼로그 처리 로직
    // ==========================================

    // 1. [카테고리 생성] 다이얼로그
    if (showAddDialog) {
        // 새 카테고리이므로 ID는 null, 기본 아이콘 설정
        val newCategoryTemplate = CategoryEditData(id = null, title = "", iconRes = R.drawable.ic_default, count = 0)

        CategoryEditDialog(
            category = newCategoryTemplate,
            onDismissRequest = { showAddDialog = false },
            onSaveClick = { name, icon ->
                Log.d("CATEGORY_API", "🆕 생성 요청: $name")
                onAddCategory(name, icon) // ViewModel의 생성 함수 호출
                showAddDialog = false
            }
        )
    }

    // 2. [카테고리 편집] 다이얼로그
    if (showEditDialog && selectedCategory != null) {
        CategoryEditDialog(
            category = selectedCategory!!,
            onDismissRequest = { showEditDialog = false },
            onSaveClick = { newName, newIcon ->
                val targetId = selectedCategory!!.id

                if (targetId != null) {
                    Log.d("CATEGORY_API", "🔄 수정 요청 ID: $targetId")
                    // (1) 서버 API 호출 (ViewModel)
                    onEditCategory(targetId, newName, newIcon)

                    // (2) 화면 즉시 갱신 (Optimistic Update)
                    val index = categoryList.indexOfFirst { it.id == targetId }
                    if (index != -1) {
                        categoryList[index] = categoryList[index].copy(
                            title = newName,
                            iconRes = newIcon
                        )
                    }
                }
                showEditDialog = false
            }
        )
    }

    // 3. [카테고리 삭제] 다이얼로그
    if (showDeleteDialog && selectedCategory != null) {
        CommonDeleteDialog(
            message = "카테고리를 삭제하시겠어요?\n포함된 낱말은 모두 삭제돼요.",
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                val targetId = selectedCategory!!.id
                if (targetId != null) {
                    Log.d("CATEGORY_API", "🗑️ 삭제 요청 ID: $targetId")
                    onDeleteCategory(targetId)
                }
                categoryList.remove(selectedCategory)
                showDeleteDialog = false
            }
        )
    }
}