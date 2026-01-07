package com.example.aac.feature.ai_sentence.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aac.R
import com.example.aac.feature.ai_sentence.ui.components.SelectedWordRow
import com.example.aac.feature.ai_sentence.ui.components.SentenceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSentenceScreen(
    onBack: () -> Unit,
    vm: AiSentenceViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 문장 완성") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "뒤로가기",
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SelectedWordRow(
                words = state.selectedWords,
                onPlayTop = { vm.onEvent(AiSentenceUiEvent.ClickPlayTop) }
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.sentences, key = { it.id }) { item ->
                    SentenceCard(
                        text = item.text,
                        isFavorite = item.isFavorite,
                        onEdit = { vm.onEvent(AiSentenceUiEvent.ClickEdit(item.id)) },
                        onFavorite = { vm.onEvent(AiSentenceUiEvent.ClickFavorite(item.id)) },
                        onPlay = { vm.onEvent(AiSentenceUiEvent.ClickPlaySentence(item.id)) }
                    )
                }
            }
        }
    }
}
