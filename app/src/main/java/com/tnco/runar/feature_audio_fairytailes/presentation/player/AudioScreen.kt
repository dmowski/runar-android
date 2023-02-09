package com.tnco.runar.feature_audio_fairytailes.presentation.player

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioDetailRow
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioHeader

@Composable
fun AudioScreen(navController: NavController) {
    val audioFairyTales: Map<String, List<String>> = mapOf(
        "Books" to listOf(
            "To Kill a Mockingbird",
            "Lord of the Flies",
            "Wuthering Heights"
        ),
        "Norwegian Fairytales" to listOf(
            "The Fisherman and the Draug",
            "The Giant Who Had No Heart in His Body",
            "Farmer Weatherbeard",
            "The King's Hares",
            "Minnikin",
            "The Master Maid"
        ),
        "Christmas Music" to listOf(
            "All I Want For Christmas Is You",
            "Rockin' Around The Christmas Tree",
            "Jingle Bell Rock",
            "A Holly Jolly Christmas",
            "Anti-Hero",
            "Unholy",
            "Superhero",
            "Last Christmas",
            "Rich Flex"
        )
    )
    AudioFairyTalesList(
        audioFairyTales = audioFairyTales,
        navController = navController
    )
}

@Composable
private fun AudioFairyTalesList(
    audioFairyTales: Map<String, List<String>>,
    navController: NavController
) {
    LazyColumn {
        audioFairyTales.onEachIndexed { index, fairyTalesEntry ->
            item {
                AudioHeader(
                    name = fairyTalesEntry.key,
                    isFirstHeader = index == 0
                )
            }

            items(fairyTalesEntry.value) { audioFairyTale ->
                AudioDetailRow(
                    audioNameText = audioFairyTale,
                    audioCategoryText = fairyTalesEntry.key,
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
