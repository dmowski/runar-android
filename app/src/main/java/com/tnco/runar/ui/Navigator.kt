package com.tnco.runar.ui

interface Navigator {
    fun navigateToFavInterpretationFragment(layoutId: Int, userLayout: IntArray, affirmId: Int)
    fun navigateToAboutFragment()
    fun navigateToDefaultAndShowBottomNavBar()
    fun navigateToFavAndShowBottomNavBar()
    fun navigateToSettings()
    fun showDialog(page: String)
    fun agreeWithDialog()

    fun getAudioFocus()
    fun dropAudioFocus()
}