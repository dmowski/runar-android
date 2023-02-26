package com.tnco.runar.ui.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.screen.OnboardScreen
import org.junit.Rule
import org.junit.Test

class OnboardTests : TestCase() {
    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    private val onboardScreen = OnboardScreen()

    @Test
    fun skipButtonTest() = run {
        step("Click on skip button") {
            onboardScreen.skipButton.click()
        }
    }
}
