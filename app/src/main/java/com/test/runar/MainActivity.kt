package com.test.runar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){
    private val model: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        model.changeFragment(LayoutFragment())
        bottomNav.setOnNavigationItemSelectedListener {
            var selectedFragment: Fragment? = null
            selectedFragment = when(it.itemId){
                R.id.nav_layouts-> LayoutFragment()
                R.id.nav_runes-> RunesFragment()
                else-> RunesFragment()
            }
            if (selectedFragment != null) {
                model.changeFragment(selectedFragment)
            }
            true
        }
        model.selectedFragment.observe(this){
            fragmentChanger(it)
        }
    }
    private fun fragmentChanger(newFragment: Fragment?){
        if (newFragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,newFragment).commit()
        }
    }

}