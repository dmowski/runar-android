package com.test.runar.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.dialogs.CancelDialog

import java.util.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var viewModel: MainViewModel
    private var readyToBack = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val model: MainViewModel by viewModels()
        viewModel = model
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            initBottomNavBar()
        }
        model.identify()
        model.getRuneDataFromDB(this)
        model.getAffirmationsDataFromDB(this)
        supportActionBar?.hide()

        model.readyToDialog.observe(this){
            readyToBack = it
        }
    }

    private fun initBottomNavBar() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        val navController = findNavController(R.id.hostFragment)
        bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.layoutFragment,R.id.favFragment -> View.VISIBLE
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
                val alert = CancelDialog(navController, this,R.id.action_layoutInitFragment_to_layoutFragment2)
                alert.showDialog()
            }
            R.id.layoutInterpretationFragment ->{
                if(readyToBack){
                    val alert = CancelDialog(navController, this,R.id.action_layoutInterpretationFragment_to_layoutFragment)
                    alert.showDialog()
                }
                else viewModel.pressBackButton(true)
            }
            R.id.layoutFragment -> android.os.Process.killProcess(android.os.Process.myPid())
            R.id.favFragment -> navController.navigate(R.id.layoutFragment)
            else -> super.onBackPressed()
        }
    }
}