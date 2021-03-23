package com.test.runar.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.test.runar.R
import com.test.runar.controllers.MusicController
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.LanguageRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.room.AppDB
import com.test.runar.ui.Navigator
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

    private var navigator: Navigator? = null

    override fun onAttach(context: Context) {
        navigator = context as Navigator
        super.onAttach(context)
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_settings, container, false)

        var preferencesRepository = SharedPreferencesRepository.get()

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

        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_rus -> {
                    setLocalRu()
                    radioButtonRus?.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.settings_radio_button)
                    )
                    radioButtonEn?.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.arrow)
                    )
                }
                R.id.radioButton_en -> {
                    setLocalEn()
                    radioButtonEn?.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.settings_radio_button)
                    )
                    radioButtonRus?.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.arrow)
                    )
                }
            }
        }

        val locale: String = Locale.getDefault().language
        when(locale){
            "ru"-> {
                radioButtonRus?.isChecked = true
            }
            else->{
                radioButtonEn?.isChecked = true
            }
        }

        when(preferencesRepository.settingsMusic){
            1->switchmusic?.isChecked=true
            0->switchmusic?.isChecked=false
        }


        switchmusic?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                preferencesRepository.changeSettingsMusic(1)
                MusicController.startMusic()
            } else {
                preferencesRepository.changeSettingsMusic(0)
                MusicController.stopMusic()
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
            navigator?.navigateToAboutFragment()
        }
        return view
    }


    private fun setLocalRu() {
        LanguageRepository.changeLanguage(requireActivity(),"ru")
        updateLanguage()
        radioButtonRus?.isChecked = true
    }


    private fun setLocalEn() {
        LanguageRepository.changeLanguage(requireActivity(),"en")
        updateLanguage()
        radioButtonEn?.isChecked = true
    }

    private fun updateLanguage(){
        context?.let { AppDB.init(it) }
        DatabaseRepository.reinit()
        context?.let { SharedDataRepository.init(it) }

        settingstxt?.setText(R.string.settings_layout)
        languagetxt?.setText(R.string.settings_language)
        musictxt?.setText(R.string.music_txt)
        ratetxt?.setText(R.string.rate_app_txt)
        abouttxt?.setText(R.string.about_app_txt)
    }
}