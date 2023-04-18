package com.tnco.runar.util

import android.content.Context
import android.os.CountDownTimer
import com.tnco.runar.repository.SharedPreferencesRepository
import java.util.*

abstract class CounterUtil(val context: Context) {
    private var timer: CountDownTimer? = null
    private val sharedPreferencesRepository = SharedPreferencesRepository(context)

    abstract fun onTimerTick(millisUntilFinished: Long)
    abstract fun onTimerFinish()

    fun startOrRefreshCounting(duration: Long = THREE_DAY_IN_MILLISECOND, countInterval: Long = 1000) {
        val countingStartDate: Long = sharedPreferencesRepository.сountingStartDate
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        val durationTimeAgo = currentTimeInMillis - duration

        // durationTimeAgo = три дня назад
        if (countingStartDate <= durationTimeAgo) {
            cancelCounting()
            onTimerFinish()
        } else {
            var beforeCountingMillis = 0L

            if (countingStartDate == 0L)
                sharedPreferencesRepository.changeStartCountingDate(currentTimeInMillis)
            else
                beforeCountingMillis = currentTimeInMillis - countingStartDate

            timer?.cancel()
            timer = object : CountDownTimer(duration - beforeCountingMillis, countInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    onTimerTick(millisUntilFinished)
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

    companion object {
        const val THREE_DAY_IN_MILLISECOND = 3600000L * 72L
    }
}
