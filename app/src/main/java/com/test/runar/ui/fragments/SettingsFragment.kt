package com.test.runar.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.runar.R
import com.test.runar.controllers.MusicController
import com.test.runar.databinding.FragmentSettingsBinding
import com.test.runar.repository.LanguageRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.ui.Navigator
import java.util.*

class SettingsFragment : Fragment() {
    private var navigator: Navigator? = null

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        navigator = context as Navigator
        super.onAttach(context)
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        _binding = FragmentSettingsBinding.bind(view)

        val preferencesRepository = SharedPreferencesRepository.get()
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_rus -> {
                    updateLanguage("ru")
                    binding.radioButtonRus.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.settings_radio_button)
                    )
                    binding.radioButtonEn.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.arrow)
                    )
                }
                R.id.radioButton_en -> {
                    updateLanguage("en")
                    binding.radioButtonEn.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.settings_radio_button)
                    )
                    binding.radioButtonRus.buttonTintList = ColorStateList.valueOf(
                        requireContext().getColor(R.color.arrow)
                    )
                }
            }
        }

        when(Locale.getDefault().language){
            "ru"-> {
                binding.radioButtonRus.isChecked = true
            }
            else->{
                binding.radioButtonEn.isChecked = true
            }
        }

        when(preferencesRepository.settingsMusic){
            1->binding.switchMusic.isChecked=true
            0->binding.switchMusic.isChecked=false
        }

        binding.switchMusic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferencesRepository.changeSettingsMusic(1)
                MusicController.startMusic()
            } else {
                preferencesRepository.changeSettingsMusic(0)
                MusicController.stopMusic()
            }
        }
        binding.imageRateApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.alexbernat.bookofchanges"
                ) // here is the uri  app in google play
                setPackage("com.android.vending")
            }
            startActivity(intent)
        }
        binding.imageAbout.setOnClickListener {
            navigator?.navigateToAboutFragment()
        }
        return view
    }

    private fun updateLanguage(language: String){
        LanguageRepository.changeLanguage(requireActivity(),language)
        binding.settings.setText(R.string.settings_layout)
        binding.settingLanguage.setText(R.string.settings_language)
        binding.musicTxt.setText(R.string.music_txt)
        binding.rateAppTxt.setText(R.string.rate_app_txt)
        binding.aboutTxt.setText(R.string.about_app_txt)
    }
}