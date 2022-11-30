package com.tnco.runar.ui.fragment

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.*
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.viewmodel.LibraryViewModel
import com.tnco.runar.util.AnalyticsConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
internal fun Bars() {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val header by viewModel.lastMenuHeader.observeAsState()
    val tabsState = remember {
        mutableStateOf(true)
    }
    val pagerState = rememberPagerState(pageCount = 2)

    var barColor = colorResource(id = R.color.library_top_bar_header)
    var barFont = FontFamily(Font(R.font.roboto_medium))
    var barFontSize = with(LocalDensity.current) { ((fontSize!! * 1.35f)).toSp() }
    var navIcon: @Composable (() -> Unit)? = null

    if (header != stringResource(id = R.string.library_top_bar_header)) {
        tabsState.value = false
        barColor = colorResource(id = R.color.library_top_bar_header_2)
        barFont = FontFamily(Font(R.font.roboto_medium))
        barFontSize = with(LocalDensity.current) { fontSize!!.toSp() }
        navIcon = { TopBarIcon() }
    } else tabsState.value = true
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = header!!,
                        color = barColor,
                        fontFamily = barFont,
                        style = TextStyle(fontSize = barFontSize)
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                navigationIcon = navIcon,
                elevation = 0.dp
            )
        },
        backgroundColor = colorResource(id = R.color.library_top_bar_2)
    ) { paddingValue ->
        val scrollState = rememberScrollState()
        if (tabsState.value && audioFeature) {
            TabScreen(pagerState, scrollState, fontSize)
        } else {
            Column(modifier = Modifier
                .padding(top = paddingValue.calculateBottomPadding())
                .verticalScroll(state = scrollState, enabled = true)) {
                ItemData(scrollState)
                Box(modifier = Modifier.aspectRatio(15f, true))
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
internal fun ItemData(scrollState: ScrollState) {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val menuData by viewModel.menuData.observeAsState()
    viewModel.updateLastMenuHeader(stringResource(id = R.string.library_top_bar_header))
    viewModel.firstMenuDataCheck()

    if (menuData != null) {
        for (item in menuData!!) {
            if (item.imageUrl.isNullOrEmpty()) item.imageUrl = ""
            when (item.type) {
                "root" -> {
                    FirstMenuItem(
                        fontSize = fontSize!!,
                        header = item.title!!,
                        text = item.content!!,
                        imgLink = item.imageUrl!!,
                        clickAction = {
                            AnalyticsHelper.sendEvent(
                                AnalyticsEvent.LIBRARY_SECTION_OPENED,
                                Pair(AnalyticsConstants.SECTION, item.title!!)
                            )
                            viewModel.addScrollPositionHistory(scrollState.value)
                            CoroutineScope(Dispatchers.IO).launch {
                                scrollState.scrollTo(0)
                            }
                            viewModel.updateMenuData(item.id)
                        }
                    )
                }

                "subMenu" -> SecondMenuItem(
                    fontSize = fontSize!!,
                    header = item.title!!,
                    imgLink = item.imageUrl!!,
                    clickAction = {
                        viewModel.addScrollPositionHistory(scrollState.value)
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(100)
                            scrollState.scrollTo(0)
                        }
                        viewModel.updateMenuData(item.id)
                    }
                )
                "poem" -> ThirdMenuItem(
                    fontSize = fontSize!!,
                    text = item.content!!,
                    title = item.title!!
                )
                "plainText" -> SimpleTextItem(
                    fontSize = fontSize!!,
                    text = item.content,
                    urlTitle = item.linkTitle,
                    urlLink = item.linkUrl
                )
                "rune" -> {
                    var imgUrl = " "
                    if (item.imageUrl != null) imgUrl = item.imageUrl!!
                    RuneDescription(
                        fontSize = fontSize!!,
                        header = item.title!!,
                        text = item.content!!,
                        imgLink = imgUrl,
                        runeTags = item.runeTags!!
                    )
                }
            }
        }
    }

    viewModel.scrollPositionHistory.observe(LocalLifecycleOwner.current) {
        CoroutineScope(Dispatchers.IO).launch {
            if (it.last() == 9999) {
                delay(450)
                scrollState.scrollTo(it[2])
                viewModel.removeLastScrollPositionHistory()
                viewModel.removeLastScrollPositionHistory()
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun FirstMenuItem(
    fontSize: Float,
    header: String,
    text: String,
    imgLink: String,
    clickAction: () -> Unit
) {
    Row(
        Modifier
            .aspectRatio(3.8f, true)
            .clickable(onClick = clickAction)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
        Column(
            Modifier
                .fillMaxSize()
                .weight(398f)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(20f)
            )
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(62f), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(imgLink),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .padding(top = 5.dp, bottom = 5.dp)
                        .weight(60f)
                        .fillMaxSize()
                )
                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(277f)
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.library_item_header),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() }),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = text,
                        color = colorResource(id = R.color.library_item_text),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(fontSize = with(LocalDensity.current)
                        { ((fontSize * 0.8f)).toSp() })
                    )
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(17f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .weight(10f)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(16f)
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(20f)
            )
            Divider(
                color = colorResource(id = R.color.divider)
            )
        }
    }
}

@Composable
private fun SecondMenuItem(
    fontSize: Float,
    header: String,
    imgLink: String,
    clickAction: () -> Unit
) {
    if (imgLink.isEmpty()) {
        Row(
            Modifier
                .fillMaxSize()
                .clickable(onClick = clickAction)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(16f)
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(398f)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .aspectRatio(24f, true)
                )
                Row(
                    Modifier
                        .fillMaxSize(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.library_item_header),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) { fontSize.toSp() },
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }),
                        modifier = Modifier
                            .weight(320f)
                            .fillMaxSize()
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(17f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x00000000))
                            .fillMaxSize()
                            .weight(10f)
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(16f)
                    )
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .aspectRatio(19f, true)
                )
                Divider(
                    color = Color(0xA6545458)
                )
            }
        }
    } else {
        Row(
            Modifier
                .aspectRatio(3.8f, true)
                .clickable(onClick = clickAction)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(4f)
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(398f)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
                Row(
                    Modifier
                        .fillMaxSize()
                        .weight(62f), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(imgLink),
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x00000000))
                            .padding(top = 5.dp, bottom = 5.dp)
                            .weight(60f)
                            .fillMaxSize()
                    )
                    Row(
                        Modifier
                            .fillMaxSize()
                            .weight(277f)
                            .padding(start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = header,
                            color = colorResource(id = R.color.library_item_header),
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            style = TextStyle(
                                fontSize = with(LocalDensity.current) { fontSize.toSp() },
                                fontWeight = FontWeight.Normal
                            ),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(17f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x00000000))
                            .weight(10f)
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(16f)
                    )
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
                Divider(
                    color = colorResource(id = R.color.divider)
                )
            }
        }
    }
}

@Composable
private fun ThirdMenuItem(fontSize: Float, text: String, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.aspectRatio(35f))
        Text(
            text = title,
            color = colorResource(id = R.color.library_third_id),
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.9f)).toSp() })
        )
        Text(
            text = text,
            color = colorResource(id = R.color.library_third_text),
            fontFamily = FontFamily(Font(R.font.roboto_light)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 0.95f)).toSp() },
                textAlign = TextAlign.Center,
                lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }),
            modifier = Modifier.padding(top = 5.dp)
        )
        Box(Modifier.aspectRatio(35f))
    }
}

@Composable
private fun TopBarIcon() {
    val viewModel: LibraryViewModel = viewModel()
    IconButton(onClick = { viewModel.goBackInMenu() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
            tint = colorResource(id = R.color.library_top_bar_fav),
            contentDescription = "arrow"
        )
    }
}



