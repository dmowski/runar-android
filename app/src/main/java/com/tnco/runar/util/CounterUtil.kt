package com.tnco.runar.util

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.tnco.runar.R
import com.tnco.runar.repository.SharedPreferencesRepository
import java.util.*

abstract class CounterUtil(val context: Context) {
    private var timer: CountDownTimer? = null
    private val sharedPreferencesRepository = SharedPreferencesRepository(context)

    abstract fun onTimerTick(timeUntilFinished: String)
    abstract fun onTimerFinish()

    fun startOrRefreshCounting(
        duration: Long = THREE_DAY_IN_MILLISECOND,
        countInterval: Long = 1000
    ) {
        val countingStartDate: Long = sharedPreferencesRepository.countingStartDate
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        val durationTimeAgo = currentTimeInMillis - duration

        // durationTimeAgo = три дня назад
        if (countingStartDate in 1..durationTimeAgo) {
            Log.d("TAG_COUNTER", "startOrRefreshCounting: Cancelling!")
            cancelCounting()
            onTimerFinish()
        } else {
            var beforeCountingMillis = 0L

            if (countingStartDate == 0L)
                sharedPreferencesRepository.changeStartCountingDate(currentTimeInMillis)
            else
                beforeCountingMillis = currentTimeInMillis - countingStartDate

            Log.d("TAG_COUNTER", "startOrRefreshCounting: $beforeCountingMillis")

            timer?.cancel()
            timer = object : CountDownTimer(duration - beforeCountingMillis, countInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    onTimerTick(
                        millisUntilFinished.toDateFormat(
                            context.getString(R.string.d),
                            context.getString(R.string.h),
                            context.getString(R.string.m)
                        )
                    )
                }

                override fun onFinish() {
                    sharedPreferencesRepository.changeStartCountingDate(0)
                    onTimerFinish()
                }
            }
            timer?.start()
        }
    }

    fun cancelCounting() {
        sharedPreferencesRepository.changeStartCountingDate(0)
        timer?.cancel()
        timer = null
    }

    private fun Long.toDateFormat(day: String, hour: String, minute: String): String {
        var temp = this

        return if ((this / DAY_IN_MILLISECOND) > 0) {
            var res = (temp / DAY_IN_MILLISECOND).toString() + day
            temp %= DAY_IN_MILLISECOND

            res += " " + (temp / HOUR_IN_MILLISECOND) + hour
            temp %= HOUR_IN_MILLISECOND

            "$res ${temp / MINUTE_IN_MILLISECOND}$minute"
        } else {
            val seconds = (this / 1000)
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60

            String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        }
    }

    companion object {
        const val MINUTE_IN_MILLISECOND = 60000L
        const val HOUR_IN_MILLISECOND = 3600000L
        const val DAY_IN_MILLISECOND = 3600000L * 24L
        const val THREE_DAY_IN_MILLISECOND = 3600000L * 72L
    }
}
