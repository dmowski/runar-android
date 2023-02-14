package com.tnco.runar.ui.screen

import com.tnco.runar.R
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView

class OnboardScreen : Screen<OnboardScreen>() {
    val skipButton = KTextView {
        withId(R.id.skip_button)
    }
}
