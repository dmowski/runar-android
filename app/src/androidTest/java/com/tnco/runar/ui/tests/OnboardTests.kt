package com.tnco.runar.ui.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.screen.OnboardScreen
import com.tnco.runar.ui.screen.SplashScreen
import org.junit.Rule
import org.junit.Test

class OnboardTests : TestCase() {
    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    @Test
    fun testSplashVisible() = run {
        step("Splash is visible") {
            SplashScreen {
                textView {
                    isVisible()
                }
            }
        }
    }

    @Test
    fun testOnboardVisibleAndClick() = run {
        step("Onboard is visible") {
            OnboardScreen {
                skipButton {
                    isVisible()
                }
            }
        }
        step("Skip Onboard") {
            OnboardScreen {
                skipButton {
                    click()
                }
            }
        }
    }
}
