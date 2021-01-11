package com.test.runar.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.runar.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initBottomNavBar()
        }
    }

    private fun initBottomNavBar() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        val navController = findNavController(R.id.hostFragment)
        bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.layoutFragment -> bottomNav.visibility = View.VISIBLE
                R.id.runesFragment -> bottomNav.visibility = View.VISIBLE
                R.id.placeholder -> bottomNav.visibility = View.VISIBLE
                R.id.placeholder2 -> bottomNav.visibility = View.VISIBLE
                R.id.placeholder3 -> bottomNav.visibility = View.VISIBLE
                else -> bottomNav.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.hostFragment)
        when(navController.currentDestination?.id){
            R.id.runesFragment-> navController.navigate(R.id.layoutFragment)
            R.id.layoutDescriptionFragment -> navController.navigate(R.id.layoutFragment)
            R.id.layoutFragment -> android.os.Process.killProcess(android.os.Process.myPid())
            else -> super.onBackPressed()
        }
    }
}