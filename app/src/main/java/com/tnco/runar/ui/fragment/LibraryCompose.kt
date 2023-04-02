package com.tnco.runar.ui.fragment

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.tnco.runar.R
import com.tnco.runar.domain.entities.LibraryItemType.*
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.screenCompose.componets.AppBar
import com.tnco.runar.ui.viewmodel.LibraryViewModel
import com.tnco.runar.util.AnalyticsConstants

@ExperimentalPagerApi
@Composable
internal fun LibraryBars(navController: NavController) {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val tabsState = remember {
        mutableStateOf(true)
    }
    val pagerState = rememberPagerState(pageCount = 2)
    val audioSwitcher by viewModel.audioSwitcher.asLiveData().observeAsState()

    tabsState.value = viewModel.currentFragmentTitle == stringResource(id = R.string.library_top_bar_header)
    Scaffold(
        topBar = {

            AppBar(
                title = viewModel.currentFragmentTitle, // header.toString(),
                navController = navController,
                showIcon = true
            )
        },
        backgroundColor = colorResource(id = R.color.settings_top_app_bar)
    ) { paddingValue ->
        val scrollState = rememberScrollState()
        if (scrollState.isScrollInProgress && scrollState.value > 0) {
            ScrollBars(scrollState)
        }
        if (tabsState.value && audioFeature && audioSwitcher?.state == true) {
            TabScreen(pagerState, scrollState, fontSize, navController)
        } else {
            Column(
                modifier = Modifier
                    .padding(top = paddingValue.calculateBottomPadding())
                    .verticalScroll(state = scrollState, enabled = true)
            ) {
                LibraryItems(navController)
                Box(modifier = Modifier.aspectRatio(15f, true))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LibraryItems(navController: NavController) {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val libraryItemList by viewModel.libraryItemList.observeAsState()
    libraryItemList?.let {
        when (it.firstOrNull()?.type) {
            ROOT -> {
                it.forEach { item ->
                    RootLibraryItem(
                        fontSize = fontSize!!,
                        header = item.title,
                        text = item.content,
                        imgLink = item.imageUrl,
                        clickAction = {
                            viewModel.analyticsHelper.sendEvent(
                                AnalyticsEvent.LIBRARY_SECTION_OPENED,
                                Pair(AnalyticsConstants.SECTION, item.title)
                            )
                            navController.navigate(
                                R.id.libraryFragment,
                                LibraryFragment.createArgs(
                                    childIdList = item.childs as? ArrayList<String>,
                                    fragmentTitle = item.title
                                )
                            )
                        }
                    )
                }
            }
            RUNE -> {
                it.forEach { item ->
                    RuneDescription(
                        fontSize = fontSize!!,
                        header = item.title,
                        text = item.content,
                        imgLink = item.imageUrl,
                        runeTags = item.runeTags
                    )
                }
            }
            SUB_MENU -> {
                it.forEach { item ->
                    FairyTales(
                        fontSize = fontSize!!,
                        header = item.title,
                        imgLink = item.imageUrl,
                        clickAction = {
                            navController.navigate(
                                R.id.libraryFragment,
                                LibraryFragment.createArgs(
                                    childIdList = item.childs as? ArrayList<String>,
                                    fragmentTitle = item.title
                                )
                            )
                        }
                    )
                }
            }
            POEM -> {
                it.forEach { item ->
                    ThirdMenuItem(
                        fontSize = fontSize!!,
                        text = item.content,
                        title = item.title
                    )
                }
            }
            PLAIN_TEXT -> {
                it.forEach { item ->
                    SimpleTextItem(
                        fontSize = fontSize!!,
                        text = item.content,
                        urlTitle = item.linkTitle,
                        urlLink = item.linkUrl
                    )
                }
            }
            NULL, null -> {}
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun RootLibraryItem(
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
                    .weight(62f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painterEmpty = painterResource(R.drawable.empty)
                val painter = rememberAsyncImagePainter(imgLink)
                val painterState = painter.state
                val viewModel: LibraryViewModel = viewModel()
                if (painterState is AsyncImagePainter.State.Error) {
                    viewModel.updateStateLoad(true)
                }
                if (viewModel.errorLoad.value == null) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x00000000))
                            .padding(top = 5.dp, bottom = 5.dp)
                            .weight(60f)
                            .fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterEmpty,
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x00000000))
                            .padding(top = 5.dp, bottom = 5.dp)
                            .weight(60f)
                            .fillMaxSize()
                    )
                }
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
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) { fontSize.toSp() }
                        ),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = text,
                        color = colorResource(id = R.color.library_item_text),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) { ((fontSize * 0.8f)).toSp() }
                        )
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

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun FirstMenuItemPreview() {
    RootLibraryItem(fontSize = 50f, header = "header", text = "text", imgLink = "") {
    }
}

@Composable
private fun FairyTales(
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
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.library_item_header),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) { fontSize.toSp() },
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }
                        ),
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
                        .weight(62f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val painterRune = rememberAsyncImagePainter(imgLink)
                    val painterEmpty = painterResource(R.drawable.slot_active)
                    val painterState = painterRune.state
                    val viewModel: LibraryViewModel = viewModel()
                    if (painterState is AsyncImagePainter.State.Error) {
                        viewModel.updateStateLoad(true)
                    }
                    if (viewModel.errorLoad.value == null) {
                        Image(
                            painter = painterRune,
                            contentDescription = null,
                            modifier = Modifier
                                .background(Color(0x00000000))
                                .padding(top = 5.dp, bottom = 5.dp)
                                .weight(60f)
                                .fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterEmpty,
                            contentDescription = null,
                            modifier = Modifier
                                .background(Color(0x00000000))
                                .padding(top = 5.dp, bottom = 5.dp)
                                .weight(60f)
                                .fillMaxSize()
                        )
                    }
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
                lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }
            ),
            modifier = Modifier.padding(top = 5.dp)
        )
        Box(Modifier.aspectRatio(35f))
    }
}

@Composable
fun ScrollBars(scrollState: ScrollState) {
    // Reference screen size
    val standartFullScreen = 648
    val standartScreenForScrool = 456
    // to get the screen size of the current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp - 3.dp
    // getting the difference between the reference screen and the actual
    val screenHeight = configuration.screenHeightDp - standartFullScreen
    val screenCurrent = screenHeight + standartScreenForScrool
    // getting how many times you need to slow down the scroll
    val countScreen: Float = scrollState.maxValue.toFloat() / screenCurrent.toFloat()
    val slowScrollState: Float = scrollState.value / countScreen

    Box(
        modifier = Modifier
            .offset(x = screenWidth, y = slowScrollState.dp)
            .clip(RoundedCornerShape(2.dp))
            .height(70.dp)
            .width(4.dp)
            .background(Color.DarkGray)
    )
}
