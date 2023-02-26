package com.tnco.runar.ui.screen

import com.kaspersky.kaspresso.screens.KScreen
import com.tnco.runar.R
import com.tnco.runar.ui.fragment.OnboardFragment
import io.github.kakaocup.kakao.text.KTextView

object OnboardScreen : KScreen<OnboardScreen>() {

    override val layoutId: Int = R.layout.fragment_onboard
    override val viewClass: Class<*> = OnboardFragment::class.java

    val skipButton = KTextView {
        withId(R.id.skip_button)
    }
}
