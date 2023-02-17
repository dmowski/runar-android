package com.tnco.runar.feature_audio_fairytailes.presentation.player

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioDetailRow
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioHeader
import com.tnco.runar.model.LibraryItemsModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AudioScreen(
    navController: NavController,
    viewModel: AudioViewModel = viewModel()
) {

    val audioFairyTales by viewModel.audioFairyTales.collectAsStateWithLifecycle()
    viewModel.getLibraryItems()

    AudioFairyTalesList(
        audioFairyTales = audioFairyTales,
        navController = navController
    )
}

@Composable
private fun AudioFairyTalesList(
    audioFairyTales: Map<LibraryItemsModel, List<LibraryItemsModel>>,
    navController: NavController
) {
    LazyColumn {
        audioFairyTales.onEachIndexed { index, fairyTalesEntry ->
            item {
                AudioHeader(
                    name = fairyTalesEntry.key.title ?: "",
                    isFirstHeader = index == 0
                )
            }

            items(fairyTalesEntry.value) { audioFairyTale ->
                AudioDetailRow(
                    audioNameText = audioFairyTale.title ?: "",
                    audioCategoryText = fairyTalesEntry.key.title ?: "",
                    time = audioFairyTale.audioDuration ?: 0,
                    navController = navController
                )
            }
        }
    }
}

@Preview
@Composable
private fun AudioScreenPreview() {
    // AudioScreen()
}
