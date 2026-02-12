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
    // ✅ [수정 1] 추가할 때 이름뿐만 아니라 아이콘(Int)도 받아야 함
    onAddCategory: (String, Int) -> Unit,
    onEditCategory: (String, String, Int) -> Unit,
    onDeleteCategory: (String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf<CategoryEditData?>(null) }

    val listState = rememberLazyListState()

    // 드래그 앤 드롭 로직 (기존 동일)
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
        item { TipBox() }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            AddCategoryButton(onClick = { showAddDialog = true })
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ✅ items에서 이미 CategoryEditListItem에 data를 넘기고 있으므로,
        // data 안에 올바른 iconRes만 들어있다면 리스트에는 자동으로 이미지가 뜹니다.
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

    // --- 다이얼로그 로직 수정 ---

    // 1. 추가 다이얼로그
    if (showAddDialog) {
        val dummyData = CategoryEditData(id = null, title = "", iconRes = R.drawable.ic_default, count = 0)
        CategoryEditDialog(
            category = dummyData,
            onDismissRequest = { showAddDialog = false },
            // ✅ [수정 2] onSaveClick에서 이름(name)과 아이콘(icon)을 둘 다 받아서 넘김
            onSaveClick = { name, icon ->
                onAddCategory(name, icon) // ViewModel이나 상위로 아이콘 정보 전달
                showAddDialog = false
            }
        )
    }

    // 2. 편집 다이얼로그
    if (showEditDialog && selectedCategory != null) {
        CategoryEditDialog(
            category = selectedCategory!!,
            onDismissRequest = { showEditDialog = false },
            // ✅ [수정 3] 편집 시에도 새로운 아이콘(newIcon)을 반영
            onSaveClick = { newName, newIcon ->
                val targetId = selectedCategory!!.id

                // (1) 서버/DB 업데이트용 콜백 호출
                if (targetId != null) {
                    onEditCategory(targetId, newName, newIcon)
                }

                // (2) 화면 즉시 갱신 (Optimistic Update)
                // 리스트에서 현재 수정 중인 아이템의 인덱스를 찾아 내용을 갈아끼웁니다.
                val index = categoryList.indexOfFirst { it == selectedCategory }
                if (index != -1) {
                    categoryList[index] = categoryList[index].copy(
                        title = newName,
                        iconRes = newIcon // 🔥 여기서 아이콘을 바꿔줘야 리스트 이미지가 바뀝니다!
                    )
                }
                showEditDialog = false
            }
        )
    }

    // 3. 삭제 다이얼로그 (기존 동일)
    if (showDeleteDialog && selectedCategory != null) {
        CommonDeleteDialog(
            message = "카테고리를\n삭제 하시겠어요?",
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                val targetId = selectedCategory!!.id
                if (targetId != null) onDeleteCategory(targetId)
                categoryList.remove(selectedCategory)
                showDeleteDialog = false
            }
        )
    }
}