package com.tnco.runar.ui

interface Navigator {
    fun navigateToDefaultAndShowBottomNavBar()
    fun showDialog(page: String)
    fun agreeWithDialog()

    fun getAudioFocus()
    fun dropAudioFocus()
}