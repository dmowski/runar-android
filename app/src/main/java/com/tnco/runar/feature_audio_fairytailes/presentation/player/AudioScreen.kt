package com.tnco.runar.feature_audio_fairytailes.presentation.player

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioDetailRow
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioHeader

@Composable
fun AudioScreen() {
    AudioFairyTalesList(audioFairyTales = emptyMap())
}

@Composable
private fun AudioFairyTalesList(audioFairyTales: Map<String, List<String>>) {
    LazyColumn {
        audioFairyTales.onEachIndexed { index, fairyTalesEntry ->
            item {
                AudioHeader(
                    name = fairyTalesEntry.key,
                    isFirstHeader = index == 0
                )
            }

            items(fairyTalesEntry.value) { audioFairyTale ->
                AudioDetailRow(name = audioFairyTale)
            }
        }
    }
}

@Preview
@Composable
private fun AudioScreenPreview() {
    AudioScreen()
}
