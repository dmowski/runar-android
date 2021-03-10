package com.test.runar.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.test.runar.R
import java.util.*

class SettingsFragment : Fragment() {

    private var radioButtonRus: RadioButton? = null
    private var radioButtonEn: RadioButton? = null
    private var radioGroup: RadioGroup? = null
    private var switchmusic:Switch?=null
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

        settingstxt = view.findViewById(R.id.settings)
        languagetxt = view.findViewById(R.id.setting_language)
        musictxt = view.findViewById(R.id.music_txt)
       ratetxt = view.findViewById(R.id.rate_app_txt)
        abouttxt = view.findViewById(R.id.about_txt)

        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_rus -> setLocalRu()

                R.id.radioButton_en -> setLocalEn()
            }
        }
  //      activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
switchmusic?.setOnCheckedChangeListener { buttonView, isChecked ->
    if (isChecked){

    }else{

    }
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
        settingstxt?.text = "Настройки"
        languagetxt?.text = "Язык"
        musictxt?.text = "Музыка"
        ratetxt?.text = "Оценить приложение"
        abouttxt?.text = "О приложении"

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
        settingstxt?.text = "Settings"
        languagetxt?.text = "Language"
        musictxt?.text = "Music"
        ratetxt?.text = "Rate the application"
        abouttxt?.text = "About"
        radioButtonEn?.isChecked = true
        radioButtonRus?.isChecked = false
    }
}