package com.tnco.runar.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tnco.runar.R
import com.tnco.runar.presentation.viewmodel.LibraryViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    val viewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.getRuneDataFromDB()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                Bars()
            }
        }
        var header = getString(R.string.library_top_bar_header)
        viewModel.lastMenuHeader.observe(viewLifecycleOwner){
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
                    if(header == getString(R.string.library_top_bar_header)) consumed = false
                }
            }
            consumed
        }
        return view
    }
}

@Composable
private fun ItemData(scrollState: ScrollState) {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val menuData by viewModel.menuData.observeAsState()

    viewModel.updateLastMenuHeader(stringResource(id = R.string.library_top_bar_header))

    viewModel.firstMenuDataCheck()

    if (menuData?.second?.size!! > 0) {
        NavigateItem(fontSize = fontSize!!, route = menuData!!.second)
    }
    if (menuData?.first != null) {
        for (item in menuData?.first!!) {
            when (item.type) {
                "root" -> {
                    FirstMenuItem(
                        fontSize = fontSize!!,
                        header = item.title!!,
                        text = item.content!!,
                        imgLink = item.imageUrl!!,
                        clickAction = {
                            CoroutineScope(Dispatchers.IO).launch {
                                scrollState.scrollTo(0)
                            }
                            viewModel.updateMenuData(item.id!!)
                        }
                    )
                }
                "subMenu" -> SecondMenuItem(
                    fontSize = fontSize!!,
                    header = item.title!!,
                    clickAction = {
                        CoroutineScope(Dispatchers.IO).launch {
                            scrollState.scrollTo(0)
                        }
                        viewModel.updateMenuData(item.id!!)
                    }
                )
                "poem" -> ThirdMenuItem(
                    fontSize = fontSize!!,
                    text = item.content!!,
                    title = item.title!!
                )
                "plainText" -> SimpleTextItem(
                    fontSize = fontSize!!,
                    text = item.content!!,
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
                        imgLink = imgUrl
                    )
                }
            }
        }
    }
}

@Composable
private fun Bars() {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val header by viewModel.lastMenuHeader.observeAsState()

    var barColor = colorResource(id = R.color.library_top_bar_header)
    var barFont = FontFamily(Font(R.font.roboto_medium))
    var barFontSize = with(LocalDensity.current) { ((fontSize!! * 1.35).toFloat()).toSp() }
    var navIcon: @Composable() (() -> Unit)? = null

    if (header != stringResource(id = R.string.library_top_bar_header)) {
        barColor = colorResource(id = R.color.library_top_bar_header_2)
        barFont = FontFamily(Font(R.font.roboto_medium))
        barFontSize = with(LocalDensity.current) { fontSize!!.toSp() }
        navIcon = { TopBarIcon() }
    }
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
                navigationIcon = navIcon
            )
        },
        backgroundColor = Color(0x73000000)
    ) {
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(state = scrollState, enabled = true)) {
            ItemData(scrollState)
            Box(modifier = Modifier.aspectRatio(15f, true))
        }
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
                CoilImage(
                    data = imgLink,
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .padding(top = 5.dp, bottom = 5.dp)
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
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.8).toFloat()).toSp() })
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
                color = Color(0xA6545458)
            )
        }
    }
}

@Composable
private fun NavigateItem(fontSize: Float, route: List<String>) {
    Row(
        Modifier
            .aspectRatio(10f, true)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
        Row(
            Modifier
                .fillMaxSize()
                .weight(398f), verticalAlignment = Alignment.CenterVertically
        ) {
            for (item in route) {
                var color = colorResource(id = R.color.library_nav_not_selected)
                if (item == route.last()) color = colorResource(id = R.color.library_nav_selected)
                Text(
                    text = item,
                    style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.7).toFloat()).toSp() }),
                    color = color,
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    modifier = Modifier.padding(end = 1.dp)
                )
            }
        }
    }
}

@Composable
private fun SecondMenuItem(fontSize: Float, header: String, clickAction: () -> Unit) {
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
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() }, lineHeight = with(LocalDensity.current) { ((fontSize * 1.4).toFloat()).toSp() }),
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
}

@Composable
private fun ThirdMenuItem(fontSize: Float, text: String, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.aspectRatio(35f))
        Text(
            text = title,
            color = colorResource(id = R.color.library_third_id),
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.9).toFloat()).toSp() })
        )
        Text(
            text = text,
            color = colorResource(id = R.color.library_third_text),
            fontFamily = FontFamily(Font(R.font.roboto_light)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 0.95).toFloat()).toSp() },
                textAlign = TextAlign.Center,
                lineHeight = with(LocalDensity.current) { ((fontSize * 1.4).toFloat()).toSp() }),
            modifier = Modifier.padding(top = 5.dp)
        )
        Box(Modifier.aspectRatio(35f))
    }
}

@Composable
private fun SimpleTextItem(fontSize: Float, text: String, urlTitle: String?, urlLink: String?) {
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
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    color = colorResource(id = R.color.library_simple_text),
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) { ((fontSize * 0.95).toFloat()).toSp() },
                        textAlign = TextAlign.Start,
                        lineHeight = with(LocalDensity.current) { ((fontSize * 1.4).toFloat()).toSp() }),
                )
            }
            if (!urlLink.isNullOrEmpty() && !urlTitle.isNullOrEmpty()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .aspectRatio(30f)
                )

                val annotatedLinkString: AnnotatedString = buildAnnotatedString {
                    val text = "$urlTitle  $urlLink"
                    append(text)
                    addStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.url_text_about_color)
                        ), start = urlTitle.length+1, end = text.length
                    )
                    addStyle(
                        style = ParagraphStyle(
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.05).toFloat()).toSp() }
                        ), start = 0, end = text.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = urlLink,
                        start = urlTitle.length+1,
                        end = text.length
                    )

                }
                val uriHandler = LocalUriHandler.current
                ClickableText(
                    text = annotatedLinkString,
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) { ((fontSize * 0.7).toFloat()).toSp() },
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        color = colorResource(
                            id = R.color.url_text_color
                        ),

                    ),
                    onClick = {
                        annotatedLinkString
                            .getStringAnnotations("URL", it, it)
                            .firstOrNull()?.let { stringAnnotation ->
                                uriHandler.openUri(stringAnnotation.item)
                            }
                    }
                )
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
private fun RuneDescription(fontSize: Float, header: String, text: String, imgLink: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.aspectRatio(30f))
        Text(
            text = header,
            color = colorResource(id = R.color.lib_run_header),
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 1.2).toFloat()).toSp() },
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
            CoilImage(
                data = imgLink,
                contentDescription = null,
                modifier = Modifier
                    .background(Color(0x00000000))
                    .weight(100f)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(152f)
            )
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
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) { ((fontSize * 0.95).toFloat()).toSp() },
                            textAlign = TextAlign.Start,
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.4).toFloat()).toSp() }),
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