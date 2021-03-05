package com.test.runar.ui.fragments

import android.content.res.loader.AssetsProvider
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.presentation.viewmodel.LibraryViewModel
import dev.chrisbanes.accompanist.coil.CoilImage

class LibraryFragment : Fragment() {
    val viewModel: LibraryViewModel by viewModels()
    var currentNav = mutableListOf<Int>()

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
        view.isFocusableInTouchMode = true
        view.requestFocus()
        viewModel.currentNav.observe(viewLifecycleOwner) {
            currentNav = it
        }

        view.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    viewModel.goBackInMenu()
                }
            }
            true
        }
        return view
    }
}

@Composable
fun ItemData() {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val currentMenu by viewModel.currentMenu.observeAsState()
    val currentNavRoute by viewModel.currentNavRoute.observeAsState()
    viewModel.firstMenuDraw()
    viewModel.updateCurrentNavRoute()

    if (currentNavRoute!!.size > 0) {
        NavigateItem(fontSize = fontSize!!, route = currentNavRoute!!)
    }
    if (currentMenu != null) {
        for (item in currentMenu!!) {
            when (item.typeView) {
                "root" -> FirstMenuItem(
                    fontSize = fontSize!!,
                    header = item.title!!,
                    text = item.text!!,
                    imgLink = item.icon!!,
                    id = item.id!!
                )
                "simpleMenu" -> SecondMenuItem(
                    fontSize = fontSize!!,
                    header = item.title!!,
                    id = item.id!!
                )
                "fullText" -> ThirdMenuItem(
                    fontSize = fontSize!!,
                    text = item.text!!,
                    title = item.title!!
                )
                "simpleText" -> SimpleTextItem(
                    fontSize = fontSize!!,
                    text = item.text!!
                )
                "rune" -> RuneDescription(fontSize = fontSize!!,
                    header = item.title!!,
                    text = item.text!!,
                    imgLink = "test",
                    id = item.id!!)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ItemData()
}

@Composable
fun Bars() {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        color = colorResource(id = R.color.library_top_bar_header),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize!! * 1.35).toFloat()).toSp() })
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                modifier = Modifier.aspectRatio(5.8f, false),
                actions = {
                    /*Icon(
                        painter = painterResource(id = R.drawable.ic_bell),
                        tint = colorResource(id = R.color.library_top_bar_bell),
                        contentDescription = "Звонок",
                        modifier = Modifier
                            .padding(end = 15.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_2),
                        tint = colorResource(id = R.color.library_top_bar_fav),
                        contentDescription = "Избранное",
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )*/
                }
            )
        },
        backgroundColor = Color(0x73000000)
    ) {
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(state = scrollState, enabled = true)) {
            ItemData()
        }
    }
}

@Composable
fun FirstMenuItem(fontSize: Float, header: String, text: String, imgLink: String, id: Int) {
    val viewModel: LibraryViewModel = viewModel()
    Row(
        Modifier
            .aspectRatio(3.8f, true)
            .clickable {
                viewModel.setCurrentMenu(id)
            }
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
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
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
            Image(
                painter = painterResource(id = R.drawable.ic_divider),
                contentDescription = null
            )
        }
    }
}

@Composable
fun NavigateItem(fontSize: Float, route: List<String>) {
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
fun SecondMenuItem(fontSize: Float, header: String, id: Int) {
    val viewModel: LibraryViewModel = viewModel()
    Row(
        Modifier
            .aspectRatio(6.3f, true)
            .clickable {
                viewModel.setCurrentMenu(id)
            }
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
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(66f), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.library_item_header),
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() }),
                    modifier = Modifier
                        .weight(320f)
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
                        .weight(10f)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(16f)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_divider),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ThirdMenuItem(fontSize: Float, text: String, title: String) {
    val newText = text.replace("\\n", "\n")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.aspectRatio(13.8f))
        Text(
            text = title,
            color = colorResource(id = R.color.library_third_id),
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.9).toFloat()).toSp() })
        )
        Text(
            text = newText,
            color = colorResource(id = R.color.library_third_text),
            fontFamily = FontFamily(Font(R.font.roboto_light)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 0.95).toFloat()).toSp() },
                textAlign = TextAlign.Center,
                lineHeight = with(LocalDensity.current) { ((fontSize * 1.4).toFloat()).toSp() }),
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
fun SimpleTextItem(fontSize: Float, text: String) {
    val newText = text.replace("\\n", "\n")
    Row {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
        Box(
            Modifier
                .fillMaxSize()
                .weight(398f)
        ) {
            Text(
                text = newText,
                color = colorResource(id = R.color.library_simple_text),
                fontFamily = FontFamily(Font(R.font.roboto_light)),
                style = TextStyle(
                    fontSize = with(LocalDensity.current) { ((fontSize * 0.95).toFloat()).toSp() },
                    textAlign = TextAlign.Start,
                    lineHeight = with(LocalDensity.current) { ((fontSize * 1.4).toFloat()).toSp() }),
            )
        }
    }
}

@Composable
fun RuneDescription(fontSize: Float, header: String, text: String, imgLink: String, id: Int){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(Modifier.aspectRatio(30f))
        Text(
            text = header,
            color = colorResource(id = R.color.library_third_id),
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 1.5).toFloat()).toSp() },
                textAlign = TextAlign.Center,
            ),
        )
        Box(Modifier.aspectRatio(30f))
        Row{
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(152f))
            CoilImage(
                data = R.drawable.test_rune,
                contentDescription = null,
                modifier = Modifier
                    .background(Color(0x00000000))
                    .weight(100f)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(152f))
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
                Column{
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_divider),
                        contentDescription = null
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