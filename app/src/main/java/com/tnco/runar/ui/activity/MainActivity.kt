package com.tnco.runar.ui.activity

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.asLiveData
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
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.ui.viewmodel.MusicControllerViewModel
import com.tnco.runar.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(), Navigator, AudioManager.OnAudioFocusChangeListener,
    OnboardFragment.OnOnboardFinishedListener {

    @Inject
    lateinit var languageRepository: LanguageRepository // TODO get rid of it
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val viewModel: MainViewModel by viewModels()
    private val musicControllerViewModel: MusicControllerViewModel by viewModels()
    var preferencesRepository = SharedPreferencesRepository.get()
    private lateinit var navController: NavController

    private lateinit var audioManager: AudioManager
    lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        installSplashScreen().apply {
//            setKeepOnScreenCondition {
//                viewModel.isLoading.value
//            }
//        }

        firebaseAnalytics = Firebase.analytics
        runBlocking { // TODO get rid of it
            val initLanguage = settingsViewModel.appLanguage.first()
            languageRepository.changeLanguage(this@MainActivity, initLanguage)
        }

        settingsViewModel.appLanguage.asLiveData().observe(this) { // TODO get rid of it
            languageRepository.changeLanguage(this, it)
            reshowBar()
        }

        // status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)
        window.navigationBarColor = getColor(R.color.library_top_bar)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                R.id.generatorStartFragment -> View.VISIBLE
                R.id.favouriteFragment -> View.VISIBLE
                R.id.settingsFragment -> View.VISIBLE
                R.id.developerOptionsFragment -> View.VISIBLE
                R.id.aboutAppFragment -> View.VISIBLE
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
        if (preferencesRepository.settingsOnboarding == 1) {
            binding.bottomNavigationBar.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, OnboardFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) {
            musicControllerViewModel.stopMusic()
        } else {
            musicControllerViewModel.startMusic()
        }
    }

    override fun onResume() {
        musicControllerViewModel.updateMainStatus(true)
        musicControllerViewModel.startMusic()
        super.onResume()
    }

    override fun onPause() {
        musicControllerViewModel.updateMainStatus(false)
        musicControllerViewModel.softStopMusic()
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun reshowBar() {
        binding.bottomNavigationBar.menu[0].title = getString(R.string.bottom_nav_layouts)
        binding.bottomNavigationBar.menu[1].title = getString(R.string.bottom_nav_library)
        binding.bottomNavigationBar.menu[2].title = getString(R.string.generator)
        binding.bottomNavigationBar.menu[3].title = getString(R.string.bottom_nav_menu)
    }

    override fun getAudioFocus() {
        audioManager.requestAudioFocus(
            this,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
    }

    override fun dropAudioFocus() {
        audioManager.abandonAudioFocus(this)
    }

    override fun onOnboardFinished() {
        supportFragmentManager.popBackStack()
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }
}
