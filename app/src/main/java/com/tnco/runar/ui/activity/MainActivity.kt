package com.tnco.runar.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.databinding.ActivityMainBinding
import com.tnco.runar.feature.MusicController
import com.tnco.runar.receivers.LanguageBroadcastReceiver
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.services.PushService
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator, AudioManager.OnAudioFocusChangeListener {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var preferencesRepository: SharedPreferencesRepository

    @Inject
    lateinit var musicController: MusicController

    @Inject
    lateinit var languageRepository: LanguageRepository

    private var languageReceiver = LanguageBroadcastReceiver()

    private lateinit var navController: NavController

    private lateinit var audioManager: AudioManager
    lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        createNotificationChannel()
        languageRepository.changeLanguageAndUpdateRepo(this, preferencesRepository.language) // set app language from settings
        // status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)
        window.navigationBarColor = getColor(R.color.library_top_bar)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.registerReceiver(
            languageReceiver,
            IntentFilter("android.intent.action.LOCALE_CHANGED")
        )

        viewModel.identify()
        supportActionBar?.hide()

        if (preferencesRepository.firstLaunch == 1) {
            preferencesRepository.changeSettingsOnboarding(0)
        }

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (preferencesRepository.settingsMusic == 1) getAudioFocus()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val bottomNavBarVisibility = when (destination.id) {
                R.id.layoutFragment -> View.VISIBLE
                R.id.libraryFragment -> View.VISIBLE
                R.id.generatorFragment -> View.VISIBLE
                R.id.favouriteFragment -> View.VISIBLE
                R.id.settingsFragment -> View.VISIBLE
                R.id.developerOptionsFragment -> View.VISIBLE
                else -> View.GONE
            }
            binding.bottomNavigationBar.visibility = bottomNavBarVisibility
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    RunarLogger.logDebug("Fetching FCM registration token failed")
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                println("token: $token")
            }
        )
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) {
            musicController.stopMusic()
        } else {
            musicController.startMusic()
        }
    }

    override fun onResume() {
        musicController.mainStatus = true
        musicController.startMusic()
        super.onResume()
    }

    override fun onPause() {
        musicController.mainStatus = false
        musicController.softStopMusic()
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun reshowBar() {
        binding.bottomNavigationBar.menu[0].title = getString(R.string.bottom_nav_layouts)
        binding.bottomNavigationBar.menu[1].title = getString(R.string.bottom_nav_library)
        binding.bottomNavigationBar.menu[2].title = getString(R.string.generator)
        binding.bottomNavigationBar.menu[3].title = getString(R.string.bottom_nav_favourites)
        binding.bottomNavigationBar.menu[4].title = getString(R.string.bottom_nav_settings)
    }

    override fun getAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .build()
            )
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    override fun dropAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_LOSS)
                    .setOnAudioFocusChangeListener(this)
                    .build()
            )
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(this)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("KEYKAK", "in create notification channel")
            val name = getString(R.string.push_general_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PushService.REMINDER_CHANNEL_ID, name, importance)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
