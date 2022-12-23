package com.tnco.runar.ui.screen

import com.tnco.runar.R
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView
import kotlin.random.Random

class GeneratorScreen : Screen<GeneratorScreen>() {

    fun randomRune() : KImageView {
        return KImageView { withIndex(Random.nextInt(14)) { withId(R.id.runeItem)} }
    }

    val generatorNav = KImageView { withIndex(2) { withId(R.id.navigation_bar_item_icon_view) } }

    val runePattern = KTextView { withId(R.id.textView) }

    val chooseRandomRunes = KTextView { withId(R.id.btn_random) }

    val btnGenerate = KTextView { withId(R.id.btn_generate) }

    val btnSelect = KTextView { withId(R.id.button_select) }

    val firstPicture = KImageView { withIndex(0) { withId(R.id.backgroundImage) } }

    val btnNext = KTextView { withId(R.id.button_next) }

    val btnSave = KImageView { withId(R.id.save) }

}