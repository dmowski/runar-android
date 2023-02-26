package com.tnco.runar.ui.screen

import com.kaspersky.kaspresso.screens.KScreen
import com.tnco.runar.R
import com.tnco.runar.ui.fragment.SplashFragment
import io.github.kakaocup.kakao.text.KTextView

object SplashScreen : KScreen<SplashScreen>() {
    override val layoutId: Int = R.layout.fragment_splash
    override val viewClass: Class<*> = SplashFragment::class.java

    val textView = KTextView {
        withId(R.id.appName)
    }
}
