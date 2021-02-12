package com.test.runar.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.dialogs.CancelDialog
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var readyToBack = true
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            initBottomNavBar()
        }
        viewModel.identify()
        viewModel.getRuneDataFromDB()
        viewModel.getAffirmationsDataFromDB()
        supportActionBar?.hide()

        viewModel.readyToDialog.observe(this) {
            readyToBack = it
        }
    }

    private fun initBottomNavBar() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        val navController = findNavController(R.id.hostFragment)
        bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.layoutFragment, R.id.favFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.hostFragment)
        when (navController.currentDestination?.id) {
            R.id.favFragment -> navController.navigate(R.id.layoutFragment)
            R.id.layoutDescriptionFragment -> navController.navigate(R.id.action_layoutDescriptionFragment_to_layoutFragment)
            R.id.layoutInitFragment -> {
                val alert = CancelDialog(
                    navController,
                    this,
                    R.id.action_layoutInitFragment_to_layoutFragment2
                )
                alert.showDialog()
            }
            R.id.layoutInterpretationFragment -> {
                if (readyToBack) {
                    val alert = CancelDialog(
                        navController,
                        this,
                        R.id.action_layoutInterpretationFragment_to_layoutFragment
                    )
                    alert.showDialog()
                } else viewModel.pressBackButton(true)
            }
            R.id.layoutFragment -> android.os.Process.killProcess(android.os.Process.myPid())
            else -> super.onBackPressed()
        }
    }
}