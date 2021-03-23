package com.test.runar.ui

interface Navigator {
    fun navigateToLayoutDescriptionFragment(layoutId: Int)
    fun navigateToLayoutInitFragment(layoutId: Int)
    fun navigateToLayoutProcessingFragment(layoutId: Int, userLayout: IntArray)
    fun navigateToInterpretationFragment(layoutId: Int, userLayout: IntArray)
    fun navigateToFavInterpretationFragment(layoutId: Int, userLayout: IntArray)
    fun navigateToAboutFragment()
    fun navigateToDefaultAndShowBottomNavBar()
    fun navigateToFavAndShowBottomNavBar()
    fun navigateToSettings()
    fun showDialog()
    fun agreeWithDialog()
}