package com.test.runar.ui.activity

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
import com.test.runar.ui.fragments.LayoutProcessingFragment
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                R.id.layoutFragment, R.id.runesFragment, R.id.placeholder, R.id.placeholder2, R.id.placeholder3 -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun onBackPressed() {
        Log.d("Log", UUID.randomUUID().toString())
        val navController = findNavController(R.id.hostFragment)
        when (navController.currentDestination?.id) {
            R.id.runesFragment -> navController.navigate(R.id.layoutFragment)
            R.id.layoutDescriptionFragment -> navController.navigate(R.id.layoutFragment)
            R.id.layoutInitFragment -> {
                val alert = CancelDialog(navController, this)
                alert.showDialog()
            }
            R.id.layoutFragment -> android.os.Process.killProcess(android.os.Process.myPid())
            R.id.emptyFragment -> navController.navigate(R.id.layoutFragment)
            else -> super.onBackPressed()
        }
    }
}