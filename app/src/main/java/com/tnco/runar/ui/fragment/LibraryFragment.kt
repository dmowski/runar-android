package com.tnco.runar.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.*
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.util.AnalyticsConstants
import com.tnco.runar.ui.viewmodel.LibraryViewModel
import com.tnco.runar.util.NetworkMonitor
import com.tnco.runar.util.observeOnce
import kotlinx.coroutines.*

const val audioFeature = false

class LibraryFragment : Fragment() {
    val viewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.getRuneDataFromDB()
        super.onCreate(savedInstanceState)
    }

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        AnalyticsHelper.sendEvent(AnalyticsEvent.LIBRARY_OPENED)
        Log.d("THIS IS ONCREATEVIEW", "ONCREATEVIEW")
        val noInternet = getString(R.string.internet_conn_error)
        viewModel.isOnline.observeOnce(viewLifecycleOwner) { online ->
            if (!online && viewModel.isCacheEmpty(requireContext())) {
                Toast.makeText(
                    requireContext(),
                    noInternet,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val view = ComposeView(requireContext()).apply {
            setContent {
                Bars()
            }
        }

        var header = getString(R.string.library_top_bar_header)
        viewModel.lastMenuHeader.observe(viewLifecycleOwner) {
            header = it
        }

        view.isFocusableInTouchMode = true
        view.requestFocus()

        view.setOnKeyListener { _, keyCode, event ->
            var consumed = false
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    viewModel.goBackInMenu()
                    consumed = true
                    if (header == getString(R.string.library_top_bar_header)) consumed = false
                }
            }
            consumed
        }
        return view
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPagerApi
@Composable
private fun Bars() {
    Log.d("THIS IS B", "BARS WAS USED")
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
        }, backgroundColor = colorResource(id = R.color.library_top_bar_2)
    ) {
        val scrollState = rememberScrollState()
        if (tabsState.value && audioFeature) TabScreen(pagerState, scrollState, fontSize)
        else {
            Column(Modifier.verticalScroll(state = scrollState, enabled = true)) {
                ItemData(scrollState)
                Box(modifier = Modifier.aspectRatio(15f, true))
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun ItemData(scrollState: ScrollState) {
    Log.d("THIS IS ID", "ITEM DATA WAS USED")
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
                    FirstMenuItem(fontSize = fontSize!!,
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
                        })
                }

                "subMenu" -> {
                    SecondMenuItem(fontSize = fontSize!!,
                        header = item.title!!,
                        imgLink = item.imageUrl!!,
                        clickAction = {
                            viewModel.addScrollPositionHistory(scrollState.value)
                            CoroutineScope(Dispatchers.IO).launch {
                                scrollState.scrollTo(0)
                            }
                            viewModel.updateMenuData(item.id)
                        })
                }

                "poem" -> {
                    ThirdMenuItem(
                        fontSize = fontSize!!, text = item.content!!, title = item.title!!
                    )
                }

                "plainText" -> {
                    SimpleTextItem(
                        fontSize = fontSize!!,
                        text = item.content,
                        urlTitle = item.linkTitle,
                        urlLink = item.linkUrl
                    )
                }

                "rune" -> {
                    RuneDescription(
                        fontSize = fontSize!!,
                        header = item.title!!,
                        text = item.content!!,
                        imgLink = item.imageUrl!!,
                        runeTags = item.runeTags!!
                    )
                }
            }
        }
    }

    viewModel.scrollPositionHistory.observe(LocalLifecycleOwner.current) {
        CoroutineScope(Dispatchers.IO).launch {
            if (it.last() == 9999) {
                delay(50)
                scrollState.scrollTo(it[it.size - 2])
                viewModel.removeLastScrollPositionHistory()
                viewModel.removeLastScrollPositionHistory()
            }
        }
    }
}

@Composable
private fun CircularProgressBar() {
    CircularProgressIndicator(
            modifier = Modifier.size(37.dp),
            color = Color.Gray,
            strokeWidth = 3.dp,
    )
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

@ExperimentalPagerApi
@Composable
private fun TabScreen(pagerState: PagerState, scrollState: ScrollState, fontSize: Float?) {
    Log.d("THIS IS TS", "TABS SCREEN WAS USED")
    Column {
        Tabs(pagerState = pagerState, fontSize = fontSize)
        TabsContent(pagerState = pagerState, scrollState = scrollState)
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, scrollState: ScrollState) {
    Log.d("THIS IS TC", "TABS CONTENT WAS USED")
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
fun Tabs(pagerState: PagerState, fontSize: Float?) {
    val list = listOf(R.string.library_tab_books, R.string.library_tab_audio)

    val scope = rememberCoroutineScope()

    Log.d("THIS IS TABS", "TABS WAS USED")

    TabRow(selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.library_top_bar),
        contentColor = colorResource(id = R.color.library_top_bar_header_2),
        divider = {
            TabRowDefaults.Divider(
                thickness = 1.dp, color = colorResource(id = R.color.library_tab_divider)
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(
                    pagerState = pagerState, tabPositions = tabPositions
                ), height = 1.dp, color = colorResource(id = R.color.library_tab_indicator)
            )
        }) {
        list.forEachIndexed { index, _ ->
            Card(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(40.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (pagerState.currentPage == index) colorResource(id = R.color.library_tab_text_selected)
                    else colorResource(id = R.color.library_top_bar)
                ),
                backgroundColor = colorResource(id = R.color.library_top_bar),

                ) {
                Tab(modifier = Modifier.height(30.dp), text = {
                    Text(
                        text = stringResource(id = list[index]).uppercase(),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        fontSize = with(LocalDensity.current) { ((fontSize!! * 0.8).toFloat()).toSp() },
                        color = if (pagerState.currentPage == index) colorResource(id = R.color.library_tab_text_selected)
                        else colorResource(id = R.color.library_tab_text_not_selected)
                    )
                }, selected = pagerState.currentPage == index, onClick = {
                    scope.launch {
                        pagerState.scrollToPage(index)
                    }
                })
            }
        }
        Spacer(modifier = Modifier.width(130.dp))
    }
}

@ExperimentalPagerApi
@Composable
private fun FirstMenuItem(
    fontSize: Float, header: String, text: String, imgLink: String, clickAction: () -> Unit
) {

    Log.d("THIS IS FMI", "FIRST MENU ITEM WAS USED")

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
                if (imgLink.isEmpty()) {
                    CircularProgressBar()
                } else {

                    Image(
                        painter = rememberImagePainter(imgLink),
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
                        style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() }),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = text,
                        color = colorResource(id = R.color.library_item_text),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.8f)).toSp() })
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
    fontSize: Float, header: String, imgLink: String, clickAction: () -> Unit
) {
    Log.d("THIS IS SMI", "SECOND MENU ITEM WAS USED")

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
                Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.library_item_header),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() },
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
                if (imgLink.isEmpty()) {
                    CircularProgressBar()
                } else {

                    Image(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x00000000))
                            .fillMaxSize()
                            .weight(10f)
                    )
                }

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
}

@Composable
private fun ThirdMenuItem(fontSize: Float, text: String, title: String) {
    Log.d("THIS IS TMI", "THIRD MENU ITEM WAS USED")
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
            style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.95f)).toSp() },
                textAlign = TextAlign.Center,
                lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }),
            modifier = Modifier.padding(top = 5.dp)
        )
        Box(Modifier.aspectRatio(35f))
    }
}

@Composable
private fun SimpleTextItem(fontSize: Float, text: String?, urlTitle: String?, urlLink: String?) {
    Log.d("THIS IS STI", "SIMPE TEXT ITEM WAS USED")
    Box(
        Modifier
            .fillMaxSize()
            .aspectRatio(70f)
    )
    Row {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
        Column(
            Modifier
                .fillMaxSize()
                .weight(382f)
        ) {
            if (!text.isNullOrEmpty()) {
                Text(
                    text = text,
                    color = colorResource(id = R.color.library_simple_text),
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.95f)).toSp() },
                        textAlign = TextAlign.Start,
                        lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }),
                )
            }
            if (!urlLink.isNullOrEmpty() && !urlTitle.isNullOrEmpty()) {
               // CircularProgressBar(urlLink)
                Box(
                    Modifier
                        .fillMaxSize()
                        .aspectRatio(30f)
                )

                val annotatedLinkString: AnnotatedString = buildAnnotatedString {
                    val annotatedText = "$urlTitle  $urlLink"
                    append(annotatedText)
                    addStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.url_text_about_color)
                        ), start = urlTitle.length + 1, end = annotatedText.length
                    )
                    addStyle(
                        style = ParagraphStyle(lineHeight = with(LocalDensity.current) { ((fontSize * 1.05f)).toSp() }),
                        start = 0,
                        end = annotatedText.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = urlLink,
                        start = urlTitle.length + 1,
                        end = annotatedText.length
                    )

                }
                val uriHandler = LocalUriHandler.current
                ClickableText(text = annotatedLinkString, style = TextStyle(
                    fontSize = with(LocalDensity.current) { ((fontSize * 0.7f)).toSp() },
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    color = colorResource(
                        id = R.color.url_text_color
                    ),

                    ), onClick = {
                    annotatedLinkString.getStringAnnotations("URL", it, it).firstOrNull()
                        ?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                })
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
    }
    Box(
        Modifier
            .fillMaxSize()
            .aspectRatio(70f)
    )
}

@Composable
private fun RuneDescription(
    fontSize: Float, header: String, text: String, imgLink: String, runeTags: List<String>
) {
    Log.d("THIS IS RD", "RUNE DESCR ITEM WAS USED")
 //   CircularProgressBar(imgLink)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.aspectRatio(30f))
        Text(
            text = header,
            color = colorResource(id = R.color.lib_run_header),
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 1.2f)).toSp() },
                textAlign = TextAlign.Center,
            ),
        )
        Box(Modifier.aspectRatio(30f))
        Row {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(152f)
            )
            Image(
                painter = rememberImagePainter(imgLink, builder = {
                    size(OriginalSize)
                }),
                contentDescription = null,
                modifier = Modifier
                    .background(Color(0x00000000))
                    .weight(120f)
                    .fillMaxWidth()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(152f)
            )
        }
        Box(Modifier.aspectRatio(30f))
        FlowRow {
            for (tag in runeTags) {
                if (tag.isNotEmpty()) {
                    Text(
                        text = tag,
                        style = TextStyle(
                            color = colorResource(R.color.lib_rune_tag_text),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(12.dp, 6.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.lib_rune_tag_border),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(12.dp, 6.dp)
                    )
                }
            }
        }
        Box(Modifier.aspectRatio(30f))
        Row {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(24f)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(366f)
            ) {
                Column {
                    Text(
                        text = text,
                        color = colorResource(id = R.color.library_third_text),
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.95f)).toSp() },
                            textAlign = TextAlign.Start,
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }),
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    Box(Modifier.aspectRatio(12f))
                    Divider(
                        color = Color(0xA6545458)
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(24f)
            )
        }
    }
}
