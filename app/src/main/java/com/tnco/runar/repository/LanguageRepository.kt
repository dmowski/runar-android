package com.tnco.runar.repository

import androidx.appcompat.app.AppCompatDelegate
import java.util.*
import javax.inject.Inject

class LanguageRepository @Inject constructor() {

    fun currentAppLanguage(): String = AppCompatDelegate.getApplicationLocales().get(0)?.language
        ?: Locale.getDefault().language
}
