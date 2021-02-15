package com.test.runar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.runar.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private var progressBarLoading: ProgressBar? = null
    private var progressBarStatus = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        progressBarLoading = findViewById(R.id.loadingProgress)

        progressBarAction()
    }

    private fun progressBarAction() {
        lifecycleScope.launch {
            while (true) {
                delay(500)

                if (progressBarStatus < 100) {
                    progressBarLoading?.setProgress(progressBarStatus)
                    progressBarStatus += 30
                } else {
                    var intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    break
                }
            }
        }
    }
}

