package com.tnco.runar.controllers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsHelper {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(){
        firebaseAnalytics = Firebase.analytics
    }

    fun interruption(page: String){
        val bundle = Bundle()
        bundle.putString("page",page)
        firebaseAnalytics.logEvent("script_interruption",bundle)
    }
}