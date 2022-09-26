package com.tnco.runar.ui.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
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
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.fragment.*
import com.tnco.runar.ui.viewmodel.MainViewModel
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), Navigator, AudioManager.OnAudioFocusChangeListener {

    private val viewModel: MainViewModel by viewModels()
    private var fontSize: Float = 0f
    private var languageReceiver = LanguageBroadcastReceiver()
    var preferencesRepository = SharedPreferencesRepository.get()
    private lateinit var navController: NavController

    private lateinit var audioManager: AudioManager
    lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        LanguageRepository.setSettingsLanguage(this)//set app language from settings
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

        viewModel.fontSize.observe(this) {
            fontSize = it
        }

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
                else -> View.GONE
            }
            binding.bottomNavigationBar.visibility = bottomNavBarVisibility
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                RunarLogger.logDebug("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            println("token: $token")

        })

    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) {
            MusicController.stopMusic()
        } else {
            MusicController.startMusic()
        }
    }

    override fun onResume() {
        MusicController.mainStatus = true
        MusicController.startMusic()
        super.onResume()
    }

    override fun onPause() {
        MusicController.mainStatus = false
        MusicController.softStopMusic()
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    override fun onBackPressed() {
//        when (supportFragmentManager.findFragmentById(R.id.fragmentContainer)) {
//            is LayoutInterpretationFavFragment -> navigateToFavAndShowBottomNavBar()
//            is AboutAppFragment -> navigateToSettings()
//            is LayoutInterpretationFragment -> showDialog("layout_interpretation")
//            is LayoutProcessingFragment -> showDialog("layout_processing")
//            !is LayoutDescriptionFragment -> showDialog("navigation_error")
//            else -> {
//                navigateToDefaultAndShowBottomNavBar()
//            }
//        }
//    }

    override fun navigateToFavInterpretationFragment(
        layoutId: Int,
        userLayout: IntArray,
        affirmId: Int
    ) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                LayoutInterpretationFavFragment.newInstance(layoutId, userLayout, affirmId)
            )
            .addToBackStack(KEY_TO_FAV_FRAGMENT_BACK)
            .commit()
        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToAboutFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AboutAppFragment())
            .commit()
        binding.bottomNavigationBar.isVisible = true
    }

    override fun showDialog(page: String) {
        CancelDialog(this, fontSize, page).showDialog()
    }

    override fun agreeWithDialog() {
        navigateToDefaultAndShowBottomNavBar()
    }

    override fun navigateToDefaultAndShowBottomNavBar() {
        supportFragmentManager.popBackStack(
            KEY_TO_LAYOUT_FRAGMENT_BACK,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        binding.bottomNavigationBar.isVisible = true
    }

    override fun navigateToFavAndShowBottomNavBar() {
        supportFragmentManager.popBackStack(
            KEY_TO_FAV_FRAGMENT_BACK,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        binding.bottomNavigationBar.isVisible = true
    }

    override fun navigateToSettings() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SettingsFragment())
            .commit()
        binding.bottomNavigationBar.isVisible = true
    }

    fun reshowBar() {
        binding.bottomNavigationBar.menu[0].title = getString(R.string.bottom_nav_layouts)
        binding.bottomNavigationBar.menu[1].title = getString(R.string.bottom_nav_library)
        binding.bottomNavigationBar.menu[2].title = getString(R.string.generator)
        binding.bottomNavigationBar.menu[3].title = getString(R.string.bottom_nav_favourites)
        binding.bottomNavigationBar.menu[4].title = getString(R.string.bottom_nav_settings)
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

    companion object {
        private const val KEY_TO_LAYOUT_FRAGMENT_BACK = "KEY_LAYOUT_FRAGMENT"
        private const val KEY_TO_FAV_FRAGMENT_BACK = "KEY_FAV_FRAGMENT"
    }
}
