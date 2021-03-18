package com.test.runar.ui.activity

import android.content.IntentFilter
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.databinding.ActivityMainBinding
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.receivers.LanguageBroadcastReceiver
import com.test.runar.ui.Navigator
import com.test.runar.ui.dialogs.CancelDialog
import com.test.runar.ui.fragments.*

class MainActivity : AppCompatActivity(), Navigator {

    private val viewModel: MainViewModel by viewModels()
    private var fontSize: Float = 0f
    private var languageReceiver = LanguageBroadcastReceiver()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)

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

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.libraryFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, LibraryFragment())
                        .addToBackStack(null)
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }
                R.id.layoutFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, LayoutFragment())
                        .addToBackStack(null)
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }
                R.id.favFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FavouriteFragment())
                        .addToBackStack(null)
                        .commit()
                    binding.bottomNavigationBar.isVisible = true
                    true
                }
                else-> false
            }
        }

    }

    override fun onResume() {
        forceBarHide()
        super.onResume()
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, LayoutFragment())
            .commit()
    }

    override fun onBackPressed() {
        val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (topFragment) {
            is LayoutFragment -> finishAndRemoveTask()
            is LibraryFragment -> RunarLogger.logDebug("todo")
            is LayoutInterpretationFavFragment -> navigateToFavAndShowBottomNavBar()
            !is LayoutDescriptionFragment -> showDialog()
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

    override fun navigateToFavInterpretationFragment(layoutId: Int, userLayout: IntArray) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LayoutInterpretationFavFragment.newInstance(layoutId, userLayout))
            .addToBackStack(null)
            .commit()
        binding.bottomNavigationBar.isVisible = false
    }

    override fun showDialog() {
        CancelDialog(this,fontSize).showDialog()
    }

    override fun agreeWithDialog() {
        navigateToDefaultAndShowBottomNavBar()
    }

    override fun navigateToDefaultAndShowBottomNavBar() {
        supportFragmentManager.popBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.bottomNavigationBar.isVisible = true
    }

    override fun navigateToFavAndShowBottomNavBar() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FavouriteFragment())
            .addToBackStack(null)
            .commit()
        binding.bottomNavigationBar.isVisible = true
    }

    fun forceBarHide(){
        val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when(topFragment){
            is LayoutFragment -> binding.bottomNavigationBar.isVisible = true
            is LibraryFragment -> binding.bottomNavigationBar.isVisible = true
            is FavouriteFragment -> binding.bottomNavigationBar.isVisible = true
            else-> binding.bottomNavigationBar.isVisible = false
        }
    }

    companion object {
        private const val KEY_TO_LAYOUT_FRAGMENT_BACK = "KEY_LAYOUT_FRAGMENT"
    }
}