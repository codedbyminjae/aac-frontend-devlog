package com.example.aac.ui.features.category

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
    categoryList: SnapshotStateList<CategoryEditData>
) {
    // 모달 상태
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) } // 추가 모달 상태

    var selectedCategory by remember { mutableStateOf<CategoryEditData?>(null) }

    val listState = rememberLazyListState()

    val reorderableState = rememberReorderableLazyListState(listState) { from, to ->
        val fromId = from.key as? String
        val toId = to.key as? String

        if (fromId != null && toId != null) {
            val fromIndex = categoryList.indexOfFirst { it.id == fromId }
            val toIndex = categoryList.indexOfFirst { it.id == toId }

            if (fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
                categoryList.apply {
                    add(toIndex, removeAt(fromIndex))
                }
            }
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

        items(items = categoryList, key = { it.id }) { item ->
            ReorderableItem(state = reorderableState, key = item.id) { isDragging ->
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

    if (showAddDialog) {
        val dummyData = CategoryEditData(
            id = "",
            title = "",
            iconRes = R.drawable.ic_default,
            count = 0
        )

        CategoryEditDialog(
            category = dummyData,
            onDismissRequest = { showAddDialog = false },
            onSaveClick = { name, icon ->
                categoryList.add(
                    CategoryEditData(
                        id = System.currentTimeMillis().toString(),
                        title = name,
                        iconRes = icon,
                        count = 0
                    )
                )
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedCategory != null) {
        CategoryEditDialog(
            category = selectedCategory!!,
            onDismissRequest = { showEditDialog = false },
            onSaveClick = { newName, newIcon ->
                val index = categoryList.indexOfFirst { it.id == selectedCategory!!.id }
                if (index != -1) {
                    categoryList[index] = categoryList[index].copy(title = newName, iconRes = newIcon)
                }
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog && selectedCategory != null) {
        CommonDeleteDialog(
            message = "카테고리를\n삭제 하시겠어요?",
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                categoryList.remove(selectedCategory)
                showDeleteDialog = false
            }
        )
    }
}