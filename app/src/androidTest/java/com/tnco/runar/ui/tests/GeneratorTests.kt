package com.tnco.runar.ui.tests

import android.view.View
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.fragment.GeneratorFragment
import com.tnco.runar.ui.screen.GeneratorScreen
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

class GeneratorTests : TestCase() {

    @get:Rule
    val rule : ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private val generatorScreen = GeneratorScreen()

    @Test
    fun test_9_choose_random_runes_generator() = run {
        step("Navigation") {
            generatorScreen.generatorNav.click()
        }
        step("Go to generator") {
            generatorScreen.runePattern.click()
        }
        step("Choose random runes") {
            Thread.sleep(2000)
            generatorScreen.chooseRandomRunes.click()
        }
        step("Next press") {
            Thread.sleep(10000)
            generatorScreen.btnSelect.click()
        }
        step("First pattern chosen") {
            generatorScreen.firstPicture.click()
        }
        step("Click next") {
            generatorScreen.btnNext.click()
        }
        step("Save pattern") {
            generatorScreen.btnSave.click()
        }

    }


    @Test
    fun test_9_choose_three_runes_generator() = run {
        step("Navigation") {
            generatorScreen.generatorNav.click()
        }
        step("Go to generator") {
            generatorScreen.runePattern.click()
        }
        step("Choose random runes") {
            Thread.sleep(2000)
            generatorScreen.randomRune().click()
            generatorScreen.randomRune().click()
            generatorScreen.randomRune().click()
            generatorScreen.btnGenerate.click()
        }
        step("Next press") {
            Thread.sleep(10000)
            generatorScreen.btnSelect.click()
        }
        step("First pattern chosen") {
            generatorScreen.firstPicture.click()
        }
        step("Click next") {
            generatorScreen.btnNext.click()
        }
        step("Save pattern") {
            generatorScreen.btnSave.click()
        }

    }
}