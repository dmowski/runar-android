package com.tnco.runar.ui.fragment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.tnco.runar.R
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
    HorizontalPager(state = pagerState) { page ->
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

            1 -> {
                // Audio Library fun will be here
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Audio", color = Color.White)
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun Tabs(pagerState: PagerState, fontSize: Float?) {
    val list = listOf(R.string.library_tab_books, R.string.library_tab_audio)

    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.library_top_bar),
        contentColor = colorResource(id = R.color.library_top_bar_header_2),
        divider = {
            TabRowDefaults.Divider(
                thickness = 1.dp,
                color = colorResource(id = R.color.library_tab_divider)
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(
                    pagerState = pagerState,
                    tabPositions = tabPositions
                ),
                height = 1.dp,
                color = colorResource(id = R.color.library_tab_indicator)
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Card(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(40.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (pagerState.currentPage == index) {
                        colorResource(id = R.color.library_tab_text_selected)
                    } else colorResource(id = R.color.library_top_bar)
                ),
                backgroundColor = colorResource(id = R.color.library_top_bar),

            ) {
                Tab(
                    modifier = Modifier.height(30.dp),
                    text = {
                        Text(
                            text = stringResource(id = list[index]).uppercase(),
                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                            fontSize = with(LocalDensity.current) {
                                ((fontSize!! * 0.8).toFloat()).toSp()
                            },
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
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(130.dp))
    }
}
