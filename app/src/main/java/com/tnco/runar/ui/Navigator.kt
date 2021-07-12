package com.tnco.runar.ui

interface Navigator {
    fun navigateToLayoutDescriptionFragment(layoutId: Int)
    fun navigateToLayoutInitFragment(layoutId: Int)
    fun navigateToLayoutProcessingFragment(layoutId: Int, userLayout: IntArray)
    fun navigateToInterpretationFragment(layoutId: Int, userLayout: IntArray)
    fun navigateToFavInterpretationFragment(layoutId: Int, userLayout: IntArray, affirmId: Int)
    fun navigateToAboutFragment()
    fun navigateToDefaultAndShowBottomNavBar()
    fun navigateToFavAndShowBottomNavBar()
    fun navigateToSettings()
    fun navigateToSacrFragment1()
    fun navigateToSacrFragment2(tipSize: Int)
    fun navigateToSacrFragment3()
    fun showDialog(page: String)
    fun agreeWithDialog()

    fun getAudioFocus()
    fun dropAudioFocus()
}