package com.test.runar.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            initBottomNavBar()
        }
        val model: MainViewModel by viewModels()
        model.identify()
    }

    private fun initBottomNavBar() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        val navController = findNavController(R.id.hostFragment)
        bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.layoutFragment, R.id.emptyFragment, R.id.favFragment, R.id.favFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun onBackPressed() {
        Log.d("Log", UUID.randomUUID().toString())
        val navController = findNavController(R.id.hostFragment)
        when (navController.currentDestination?.id) {
            R.id.emptyFragment -> navController.navigate(R.id.layoutFragment)
            R.id.layoutDescriptionFragment -> navController.navigate(R.id.layoutFragment)
            R.id.layoutInitFragment -> {
                val alert = CancelDialog(navController, this)
                alert.showDialog()
            }
            R.id.layoutFragment -> android.os.Process.killProcess(android.os.Process.myPid())
            R.id.favFragment -> navController.navigate(R.id.layoutFragment)
            else -> super.onBackPressed()
        }
    }
}