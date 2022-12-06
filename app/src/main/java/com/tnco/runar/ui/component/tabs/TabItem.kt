package com.tnco.runar.ui.component.tabs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.tnco.runar.R
import com.tnco.runar.ui.fragment.AudioScreen
import com.tnco.runar.ui.fragment.BooksScreen

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(@StringRes val title: Int, var screen: ComposableFun) {

    object Books : TabItem(R.string.books, { BooksScreen() })
    object AudioTales : TabItem(R.string.audio, { AudioScreen() })
}
