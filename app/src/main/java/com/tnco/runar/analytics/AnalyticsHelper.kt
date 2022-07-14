package com.tnco.runar.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

const val RUNE_OPENED = "rune_open"
const val RUNE_VIEWED = "rune_viewed"
const val FAVOURITE_OPENED = "favourite_opened"
const val LIBRARY_OPENED = "library_opened"
const val OB_ABOUT_OPENED = "ob1_about_opened"
const val OB_FORTUNE_OPENED = "ob2_fortune_opened"
const val OB_INTERPRETATION_OPENED = "ob3_interpretation_opened"
const val OB_FAVOURITES_OPENED = "ob4_favourites_opened"
const val OB_LIBRARY_OPENED = "ob5_library_opened"
const val OB_NEXT = "ob_next"
const val OB_SKIP = "ob_skip"
const val OB_START = "ob_done_and_start"
private const val DRAW = "draw"
const val DRAWS_SELECTED = "draws_selected"
const val DRAWS_STARTED = "draws_started"
const val INTERPRETATION_STARTED = "interpretation_started"
const val INTERPRETATION_VIEWED = "interpretation_viewed"
const val DRAWS_SAVED = "draws_saved"
const val FAVOURITE_DRAWS_OPENED = "favourite_draws_opened"
const val FAVOURITE_DRAWS_DELETED = "favourite_draws_deleted"

object AnalyticsHelper {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init() {
        firebaseAnalytics = Firebase.analytics
    }

    fun interruption(page: String) {
        val bundle = Bundle()
        bundle.putString("page", page)
        firebaseAnalytics.logEvent("script_interruption", bundle)
    }

    fun musicLinkOpened(group: String, trackName: String) {
        val bundle = Bundle()
        bundle.putString("group_name", group)
        bundle.putString("track_name", trackName)
        firebaseAnalytics.logEvent("music_link_opened", bundle)
    }

    fun librarySectionOpened(section: String) {
        val bundle = Bundle()
        bundle.putString("section", section)
        firebaseAnalytics.logEvent("library_section_opened", bundle)
    }

    fun sendEvent(event: String) {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(event, bundle)
    }

    fun sendEventDraw(event: String, type: Int) {
        val bundle = Bundle()
        bundle.putString(DRAW, convertLayoutIdToName(type))
        firebaseAnalytics.logEvent(event, bundle)
    }

    private fun convertLayoutIdToName(id: Int): String {
        val layoutName = when (id) {
            1 -> "rune_of_the_day"
            2 -> "two_runes"
            3 -> "norns"
            4 -> "brief_prophecy"
            5 -> "thors_hammer"
            6 -> "cross"
            7 -> "cross_of_elements"
            8 -> "celtic_cross"
            else -> "error"
        }
        return layoutName
    }
}