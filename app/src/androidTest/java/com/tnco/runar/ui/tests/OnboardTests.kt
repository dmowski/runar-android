package com.tnco.runar.ui.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.tnco.runar.ui.activity.OnboardActivity
import com.tnco.runar.ui.screen.OnboardScreen
import org.junit.Rule
import org.junit.Test

class OnboardTests : TestCase() {
    @get:Rule
    var rule: ActivityScenarioRule<OnboardActivity> =
        ActivityScenarioRule(OnboardActivity::class.java)

    private val onboardScreen = OnboardScreen()

    @Test
    fun skipButtonTest() = run {
        step("Click on skip button") {
            onboardScreen.skipButton.click()
        }
    }
}
