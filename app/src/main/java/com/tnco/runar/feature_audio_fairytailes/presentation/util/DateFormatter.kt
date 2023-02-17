package com.tnco.runar.feature_audio_fairytailes.presentation.util

fun toMinSecFormat(totalTimeInSeconds: Int): String {
    return buildString {
        append("${totalTimeInSeconds / 60}")
        append(":")
        append(String.format("%02d", totalTimeInSeconds - (60 * (totalTimeInSeconds / 60))))
    }
}
