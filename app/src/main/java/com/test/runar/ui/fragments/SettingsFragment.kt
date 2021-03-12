package com.test.runar.ui.fragments

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.test.runar.R
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.room.AppDB
import com.test.runar.service.MediaService
import java.util.*

class SettingsFragment : Fragment() {

    private var radioButtonRus: RadioButton? = null
    private var radioButtonEn: RadioButton? = null
    private var radioGroup: RadioGroup? = null
    private var switchmusic: SwitchMaterial? = null
    private var rateapp: ImageView? = null
    private var aboutapp: ImageView? = null
    private var settingstxt: TextView? = null
    private var languagetxt: TextView? = null
    private var musictxt: TextView? = null
    private var ratetxt: TextView? = null
    private var abouttxt: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_settings, container, false)

        radioButtonRus = view.findViewById(R.id.radioButton_rus)
        radioButtonEn = view.findViewById(R.id.radioButton_en)
        radioGroup = view.findViewById(R.id.radioGroup)
        switchmusic = view.findViewById(R.id.switch_music)
        rateapp = view.findViewById(R.id.image_rate_app)
        aboutapp = view.findViewById(R.id.image_about)

        settingstxt = view.findViewById(R.id.settings)
        languagetxt = view.findViewById(R.id.setting_language)
        musictxt = view.findViewById(R.id.music_txt)
        ratetxt = view.findViewById(R.id.rate_app_txt)
        abouttxt = view.findViewById(R.id.about_txt)

        var locale: String = Locale.getDefault().language
        if (locale.equals("ru")) {
            radioButtonRus?.isChecked = true
        } else {
            radioButtonEn?.isChecked = true
        }

        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_rus -> setLocalRu()
                R.id.radioButton_en -> setLocalEn()
            }
        }
        //      activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        switchmusic?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activity?.applicationContext?.startService(
                    Intent(
                        activity?.applicationContext,
                        MediaService::class.java
                    )
                )
            } else {

                activity?.applicationContext?.stopService(
                    Intent(
                        activity?.applicationContext,
                        MediaService::class.java
                    )
                )
            }
        }
        rateapp?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.alexbernat.bookofchanges"
                ) // here is the uri  app in google play
                setPackage("com.android.vending")
            }
            startActivity(intent)
        }
        aboutapp?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, AboutAppFragment())?.addToBackStack(null)
                ?.commit()
        }
        return view
    }


    private fun setLocalRu() {
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val config: Configuration? = activity?.baseContext?.resources?.configuration
        config?.locale = locale
        activity?.baseContext?.resources?.updateConfiguration(
            config,
            activity?.baseContext?.resources?.displayMetrics
        )
        context?.let { AppDB.init(it) }
        DatabaseRepository.reinit()
        context?.let { SharedDataRepository.init(it) }

        settingstxt?.setText(R.string.settings_layout)
        languagetxt?.setText(R.string.settings_language)
        musictxt?.setText(R.string.music_txt)
        ratetxt?.setText(R.string.rate_app_txt)
        abouttxt?.setText(R.string.about_app_txt)

        radioButtonRus?.isChecked = true
        radioButtonEn?.isChecked = false
    }


    private fun setLocalEn() {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val config: Configuration? = activity?.baseContext?.resources?.configuration
        config?.locale = locale
        activity?.baseContext?.resources?.updateConfiguration(
            config,
            activity?.baseContext?.resources?.displayMetrics
        )
        context?.let { AppDB.init(it) }
        DatabaseRepository.reinit()
        context?.let { SharedDataRepository.init(it) }

        settingstxt?.setText(R.string.settings_layout)
        languagetxt?.setText(R.string.settings_language)
        musictxt?.setText(R.string.music_txt)
        ratetxt?.setText(R.string.rate_app_txt)
        abouttxt?.setText(R.string.about_app_txt)
        radioButtonEn?.isChecked = true
        radioButtonRus?.isChecked = false
    }
}