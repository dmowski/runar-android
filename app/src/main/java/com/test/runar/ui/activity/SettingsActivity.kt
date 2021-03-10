package com.test.runar.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.runar.R
import com.test.runar.ui.Navigator
import com.test.runar.ui.fragments.LayoutFragment
import com.test.runar.ui.fragments.LibraryFragment
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import java.util.*

class SettingsActivity :  AppCompatActivity() {

    private var radioButtonRus: RadioButton? = null
    private var radioButtonEn: RadioButton? = null
    private var navigation:BottomNavigationView?=null

    private val localeDelegate: LocaleHelperActivityDelegate = LocaleHelperActivityDelegateImpl()
    private val localeRus = Locale("ru","RU")

    override fun getDelegate() = localeDelegate.getAppCompatDelegate(super.getDelegate())

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localeDelegate.attachBaseContext(newBase))
    }


    override fun onResume() {
        super.onResume()
        localeDelegate.onResumed(this)
    }

    override fun onPause() {
        super.onPause()
        localeDelegate.onPaused()
    }

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        val context = super.createConfigurationContext(overrideConfiguration)
        return LocaleHelper.onAttach(context)
    }

    override fun getApplicationContext(): Context =
        localeDelegate.getApplicationContext(super.getApplicationContext())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeDelegate.onCreate(this)
        setContentView(R.layout.settings_layout)

        radioButtonRus = findViewById(R.id.radioButton_rus)
        radioButtonEn = findViewById(R.id.radioButton_en)
        navigation = findViewById(R.id.bottomNavigationBar)

        radioButtonRus?.setOnClickListener(radioButtonClickListener)
        radioButtonEn?.setOnClickListener(radioButtonClickListener)
        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.libraryFragment->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LibraryFragment())
                    .addToBackStack(null)
                    .commit()
              navigation?.isVisible = true
                true
            }
            R.id.layoutFragment->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LayoutFragment())
                    .addToBackStack(null)
                    .commit()
               navigation?.isVisible = true
                true
            }

        }
        false
    }
    var radioButtonClickListener =
        View.OnClickListener { v ->
            val rb = v as RadioButton
            when (rb.id) {
                R.id.radioButton_rus -> localeDelegate.setLocale(this, localeRus)


                R.id.radioButton_en ->localeDelegate.setLocale(this, Locale.ENGLISH)

            }
        }

}