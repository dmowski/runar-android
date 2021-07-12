package com.tnco.runar.ui.activity

import android.content.*
import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.tnco.runar.R
import com.tnco.runar.controllers.MusicController
import com.tnco.runar.databinding.ActivityMainBinding
import com.tnco.runar.presentation.viewmodel.MainViewModel
import com.tnco.runar.receivers.LanguageBroadcastReceiver
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.dialogs.CancelDialog
import com.tnco.runar.ui.fragments.*

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), Navigator, AudioManager.OnAudioFocusChangeListener {

    private val viewModel: MainViewModel by viewModels()
    private var fontSize: Float = 0f
    private var languageReceiver = LanguageBroadcastReceiver()
    var preferencesRepository = SharedPreferencesRepository.get()

    private lateinit var audioManager: AudioManager
    private lateinit var binding: ActivityMainBinding

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

        if (savedInstanceState == null) {
            initFragments()
        }
        this.registerReceiver(languageReceiver, IntentFilter("android.intent.action.LOCALE_CHANGED"))

        viewModel.identify()
        supportActionBar?.hide()

        viewModel.fontSize.observe(this){
            fontSize = it
        }

        if(preferencesRepository.firstLaunch==1){
            preferencesRepository.changeSettingsOnboarding(0)
        }

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (preferencesRepository.settingsMusic==1) getAudioFocus()



        binding.bottomNavigationBar.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.libraryFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, LibraryFragment())
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }
                R.id.layoutFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, LayoutFragment())
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }

                R.id.settingsFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SettingsFragment())
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }
                R.id.favFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FavouriteFragment())
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }
                else-> false
            }
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if(focusChange<=0){
            MusicController.stopMusic()
        }
        else{
            MusicController.startMusic()
        }
    }


    override fun onResume() {
        MusicController.mainStatus = true
        MusicController.startMusic()
        forceBarHide()
        super.onResume()
    }

    override fun onPause() {
        MusicController.mainStatus = false
        MusicController.softStopMusic()
        super.onPause()
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, LayoutFragment())
            .commit()
    }

    override fun onBackPressed() {
        val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (topFragment) {
            is LayoutFragment -> {
                finishAndRemoveTask()
                System.exit(0)
            }
            is LibraryFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LayoutFragment())
                    .commit()
                binding.bottomNavigationBar.selectedItemId = R.id.layoutFragment
            }
            is FavouriteFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LayoutFragment())
                    .commit()
                binding.bottomNavigationBar.selectedItemId = R.id.layoutFragment
            }
            is SettingsFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LayoutFragment())
                    .commit()
                binding.bottomNavigationBar.selectedItemId = R.id.layoutFragment
            }
            is LayoutInterpretationFavFragment -> navigateToFavAndShowBottomNavBar()
            is AboutAppFragment -> navigateToSettings()
            is LayoutInitFragment -> showDialog("layout_init")
            is LayoutInterpretationFragment -> showDialog("layout_interpretation")
            is LayoutProcessingFragment -> showDialog("layout_processing")
            !is LayoutDescriptionFragment -> showDialog("navigation_error")
            else -> { navigateToDefaultAndShowBottomNavBar() }
        }
    }

    override fun navigateToLayoutDescriptionFragment(layoutId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LayoutDescriptionFragment.newInstance(layoutId))
            .addToBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK)
            .commit()

        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToLayoutInitFragment(layoutId: Int) {
        val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (topFragment) {
            is LayoutFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LayoutInitFragment.newInstance(layoutId))
                    .addToBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK)
                    .commit()
            }
            is LayoutDescriptionFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LayoutInitFragment.newInstance(layoutId))
                    .addToBackStack(null)
                    .commit()
            }
            else -> throw IllegalArgumentException()
        }

        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToLayoutProcessingFragment(layoutId: Int, userLayout: IntArray) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LayoutProcessingFragment.newInstance(layoutId, userLayout))
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToInterpretationFragment(layoutId: Int, userLayout: IntArray) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LayoutInterpretationFragment.newInstance(layoutId, userLayout))
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToFavInterpretationFragment(layoutId: Int, userLayout: IntArray, affirmId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LayoutInterpretationFavFragment.newInstance(layoutId, userLayout, affirmId))
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

    override fun navigateToSacrFragment1() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SacrFragment1())
            .addToBackStack(KEY_TO_INTERPRETATION_FRAGMENT_BACK)
            .commit()
        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToSacrFragment2(tipSize: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SacrFragment2().apply {
                arguments = bundleOf(
                    "tip_size" to tipSize
                )
            })
            .addToBackStack(KEY_TO_INTERPRETATION_FRAGMENT_BACK)
            .commit()
        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToSacrFragment3() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SacrFragment3())
            .addToBackStack(KEY_TO_INTERPRETATION_FRAGMENT_BACK)
            .commit()
        binding.bottomNavigationBar.isVisible = false
    }

    override fun showDialog(page: String) {
        CancelDialog(this,fontSize,page).showDialog()
    }

    override fun agreeWithDialog() {
        navigateToDefaultAndShowBottomNavBar()
    }

    override fun navigateToDefaultAndShowBottomNavBar() {
        supportFragmentManager.popBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.bottomNavigationBar.isVisible = true
    }

    override fun navigateToFavAndShowBottomNavBar() {
        supportFragmentManager.popBackStack(KEY_TO_FAV_FRAGMENT_BACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.bottomNavigationBar.isVisible = true
    }

    fun navigateToInterpretation(){
        supportFragmentManager.popBackStack(KEY_TO_INTERPRETATION_FRAGMENT_BACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun navigateToSettings() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SettingsFragment())
            .commit()
        binding.bottomNavigationBar.isVisible = true
    }

    fun forceBarHide(){
        val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (topFragment) {
            is LayoutFragment -> binding.bottomNavigationBar.isVisible = true
            is LibraryFragment -> binding.bottomNavigationBar.isVisible = true
            is FavouriteFragment -> binding.bottomNavigationBar.isVisible = true
            is SettingsFragment -> binding.bottomNavigationBar.isVisible = true
            is AboutAppFragment -> binding.bottomNavigationBar.isVisible = true
            else-> binding.bottomNavigationBar.isVisible = false
        }
    }

    fun reshowBar(){
        binding.bottomNavigationBar.menu[0].title = getString(R.string.bottom_nav_layouts)
        binding.bottomNavigationBar.menu[1].title = getString(R.string.bottom_nav_library)
        binding.bottomNavigationBar.menu[2].title = getString(R.string.bottom_nav_favourites)
        binding.bottomNavigationBar.menu[3].title = getString(R.string.bottom_nav_settings)
    }

    override fun getAudioFocus(){
        audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN)
    }
    override fun dropAudioFocus(){
        audioManager.abandonAudioFocus (this)
    }

    companion object {
        private const val KEY_TO_LAYOUT_FRAGMENT_BACK = "KEY_LAYOUT_FRAGMENT"
        private const val KEY_TO_INTERPRETATION_FRAGMENT_BACK = "KEY_INTERPRETATION_FRAGMENT"
        private const val KEY_TO_FAV_FRAGMENT_BACK = "KEY_FAV_FRAGMENT"
        private const val KEY_TO_SETTINGS_FRAGMENT_BACK = "KEY_SETTINGS_FRAGMENT"
    }

}
