package com.tnco.runar.controllers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.tnco.runar.R

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

    fun drawsSelected(type: Int) {
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("draws_selected", bundle)
    }

    fun drawsStarted(type: Int) {
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("draws_started", bundle)
    }

    fun runeOpened() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent("rune_opened", bundle)
    }

    fun interpretationStarted(type: Int){
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("interpretation_started", bundle)
    }

    fun musicLinkOpened(group: String,trackName: String){
        val bundle = Bundle()
        bundle.putString("group_name", group)
        bundle.putString("track_name", trackName)
        firebaseAnalytics.logEvent("music_link_opened", bundle)
    }

    fun interpretationViewed(type: Int){
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("interpretation_viewed", bundle)
    }

    fun runeView(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("rune_viewed", bundle)
    }

    fun drawsSaved(type: Int){
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("draws_saved", bundle)
    }

    fun favouriteOpened(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("favourite_opened", bundle)
    }

    fun favouriteDrawsOpened(type: Int){
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("favourite_draws_opened", bundle)
    }

    fun favouriteDrawsDeleted(type: Int){
        val bundle = Bundle()
        bundle.putString("draw", convertLayoutIdToName(type))
        firebaseAnalytics.logEvent("favourite_draws_deleted", bundle)
    }

    fun libraryOpened(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("library_opened", bundle)
    }

    fun librarySectionOpened(section: String){
        val bundle = Bundle()
        bundle.putString("section", section)
        firebaseAnalytics.logEvent("library_section_opened", bundle)
    }

    fun ob1(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob1_about_opened", bundle)
    }

    fun ob2(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob2_fortune_opened", bundle)
    }

    fun ob3(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob3_interpretation_opened", bundle)
    }

    fun ob4(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob4_favourites_opened", bundle)
    }

    fun ob5(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob5_library_opened", bundle)
    }

    fun obNext(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob_next", bundle)
    }

    fun obSkip(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob_skip", bundle)
    }

    fun obStart(){
        val bundle = Bundle()
        firebaseAnalytics.logEvent("ob_done_and_start", bundle)
    }


    private fun convertLayoutIdToName(id:Int): String{
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