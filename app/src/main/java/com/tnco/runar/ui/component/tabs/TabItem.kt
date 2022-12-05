package com.tnco.runar.ui.component.tabs

import androidx.compose.runtime.Composable
import com.tnco.runar.ui.fragment.AudioScreen
import com.tnco.runar.ui.fragment.BooksScreen

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var title: String, var screen: ComposableFun) {
    object Books : TabItem("Книги", { BooksScreen() })
    object AudioTales : TabItem("Аудио", { AudioScreen() })
}
