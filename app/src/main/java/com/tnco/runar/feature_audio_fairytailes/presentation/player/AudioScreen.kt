package com.tnco.runar.feature_audio_fairytailes.presentation.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioDetailRow
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.Header

@Composable
fun AudioScreen() {
    val audioFairyTales: Map<String, List<String>> = mapOf(
        "Книги" to listOf("Book 1", "Book 2", "Book 3"),
        "Норвежские сказки" to listOf("Norwegian 1", "Norwegian 2", "Norwegian 3", "Norwegian 4", "Norwegian 5", "Norwegian 6"),
        "Музыка" to listOf("Music 1", "Music 2", "Music 3", "Music 4", "Music 5", "Music 6", "Music 7", "Music 8", "Music 9"),
        "Животные" to listOf("Animals 1", "Animals 2", "Animals 3", "Animals 4", "Animals 5", "Animals 6", "Animals 7", "Animals 8", "Animals 9")
    )
    AudioFairyTalesList(audioFairyTales = audioFairyTales)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioFairyTalesList(audioFairyTales: Map<String, List<String>>) {
    LazyColumn {
        audioFairyTales.forEach { (header, audioFairyTales) ->
            stickyHeader {
                Header(name = header)
            }

            items(audioFairyTales) { audioFairyTale ->
                AudioDetailRow(name = audioFairyTale)
            }
        }
    }
}

@Preview
@Composable
fun AudioScreenPreview() {
    AudioScreen()
}
