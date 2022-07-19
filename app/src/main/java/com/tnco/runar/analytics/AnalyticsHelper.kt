package com.tnco.runar.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.tnco.runar.enums.AnalyticsEvent

object AnalyticsHelper {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init() {
        firebaseAnalytics = Firebase.analytics
    }

    fun sendEvent(eventName: AnalyticsEvent, vararg keyValuePairs: Pair<String, String>) {
        val bundle = Bundle()
        keyValuePairs.forEach { bundle.putString(it.first, it.second) }
        firebaseAnalytics.logEvent(eventName.analyticsName, bundle)
    }

}