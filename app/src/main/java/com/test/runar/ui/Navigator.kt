package com.test.runar.ui

interface Navigator {
    fun navigateToLayoutDescriptionFragment(layoutId: Int)
    fun navigateToLayoutInitFragment(layoutId: Int)
    fun navigateToLayoutProcessingFragment(layoutId: Int, userLayout: IntArray)
    fun navigateToInterpretationFragment(layoutId: Int, userLayout: IntArray)
    fun showDialog()
    fun agreeWithDialog()
}