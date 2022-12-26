package com.tnco.runar.ui.fragment

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.tnco.runar.R
import com.tnco.runar.feature_audio_fairytailes.presentation.player.AudioScreen
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
internal fun TabScreen(pagerState: PagerState, scrollState: ScrollState, fontSize: Float?) {
    Column {
        Tabs(pagerState = pagerState, fontSize = fontSize)
        TabsContent(pagerState = pagerState, scrollState = scrollState)
    }
}

@ExperimentalPagerApi
@Composable
private fun TabsContent(pagerState: PagerState, scrollState: ScrollState) {
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        when (page) {
            0 -> {
                Column(
                    Modifier
                        .verticalScroll(state = scrollState, enabled = true)
                        .fillMaxWidth()
                ) {
                    ItemData(scrollState)
                    Box(modifier = Modifier.aspectRatio(15f, true))
                }
            }

            1 -> AudioScreen()
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun Tabs(pagerState: PagerState, fontSize: Float?) {
    val list = listOf(R.string.library_tab_books, R.string.library_tab_audio)
    val scope = rememberCoroutineScope()

    TabRow(
        modifier = Modifier
            .width(400.dp)
            .height(34.dp),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.library_top_bar_2),
        contentColor = colorResource(id = R.color.library_top_bar_header_2),
        indicator = {}
    ) {
        list.forEachIndexed { index, _ ->
            Card(
                backgroundColor = if (pagerState.currentPage == index) {
                    colorResource(id = R.color.library_tab_background_selected)
                } else {
                    colorResource(id = R.color.library_tab_background_not_selected)
                }
            ) {
                Tab(
                    text = {
                        Text(
                            text = stringResource(id = list[index]),
                            color = if (pagerState.currentPage == index) {
                                colorResource(id = R.color.library_tab_text_selected)
                            } else {
                                colorResource(id = R.color.library_tab_text_not_selected)
                            }
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
    }
}
