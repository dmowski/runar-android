package com.test.runar

import android.util.Log

object RunarLogger {

    private const val TAG = "RunarLogger"

    fun logError(message: String?, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}