package com.tnco.runar.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.tnco.runar.enums.AnalyticsEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun sendEvent(eventName: AnalyticsEvent, vararg keyValuePairs: Pair<String, String>) {
        val bundle = Bundle()
        keyValuePairs.forEach { bundle.putString(it.first, it.second) }
        firebaseAnalytics.logEvent(eventName.analyticsName, bundle)
    }
}
