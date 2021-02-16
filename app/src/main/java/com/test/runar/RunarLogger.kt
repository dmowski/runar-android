package com.test.runar

import android.util.Log

object RunarLogger {

    private const val TAG = "RunarLogger"

    fun logError(message: String? = "empty message", throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
    fun logDebug(message: String = "empty message") {
        Log.d(TAG, message)
    }
}