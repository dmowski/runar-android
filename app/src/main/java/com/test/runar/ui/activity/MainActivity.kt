package com.test.runar.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.databinding.ActivityMainBinding
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.Navigator
import com.test.runar.ui.dialogs.CancelDialog
import com.test.runar.ui.fragments.*

class MainActivity : AppCompatActivity(), Navigator {

    private val viewModel: MainViewModel by viewModels()
    private var readyToBack = true

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            initFragments()
        }

        viewModel.identify()
        viewModel.getRuneDataFromDB()
        viewModel.getAffirmationsDataFromDB()
        supportActionBar?.hide()

        viewModel.readyToDialog.observe(this) {
            readyToBack = it
        }
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, LayoutFragment())
            .commit()
    }

    override fun onBackPressed() {
        val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (topFragment) {
            is LayoutFragment -> super.onBackPressed()
            !is LayoutDescriptionFragment -> showDialog()
            else -> { navigateToDefaultAndShowBottomNavBar() }
        }
    }

    override fun navigateToLayoutDescriptionFragment(layoutId: Int) {
        supportFragmentManager.beginTransaction()
            .hide(supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!)
            .add(R.id.fragmentContainer, LayoutDescriptionFragment.newInstance(layoutId))
            .addToBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK)
            .commit()

        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToLayoutInitFragment(id: Int) {
        when (val topFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)) {
            is LayoutFragment -> {
                supportFragmentManager.beginTransaction()
                    .hide(topFragment)
                    .add(R.id.fragmentContainer, LayoutInitFragment.newInstance(id))
                    .addToBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK)
                    .commit()
            }
            is LayoutDescriptionFragment -> {
                supportFragmentManager.beginTransaction()
                    .hide(topFragment)
                    .add(R.id.fragmentContainer, LayoutInitFragment.newInstance(id))
                    .addToBackStack(null)
                    .commit()
            }
            else -> throw IllegalArgumentException()
        }

        binding.bottomNavigationBar.isVisible = false
    }

    override fun navigateToLayoutProcessingFragment(id: Int, userLayout: IntArray) {
        supportFragmentManager.beginTransaction()
            .hide(supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!)
            .add(R.id.fragmentContainer, LayoutProcessingFragment.newInstance(id, userLayout))
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToInterpretationFragment(id: Int, userLayout: IntArray) {
        supportFragmentManager.beginTransaction()
            .hide(supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!)
            .add(R.id.fragmentContainer, LayoutInterpretationFragment.newInstance(id, userLayout))
            .addToBackStack(null)
            .commit()
    }

    override fun showDialog() {
        CancelDialog(this).showDialog()
    }

    override fun agreeWithDialog() {
        navigateToDefaultAndShowBottomNavBar()
    }

    private fun navigateToDefaultAndShowBottomNavBar() {
        supportFragmentManager.popBackStack(KEY_TO_LAYOUT_FRAGMENT_BACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.bottomNavigationBar.isVisible = true
    }

    companion object {
        private const val KEY_TO_LAYOUT_FRAGMENT_BACK = "KEY_LAYOUT_FRAGMENT"
    }
}