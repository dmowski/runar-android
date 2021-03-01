package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
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

class LibraryFragment : Fragment() {
    val viewModel: LibraryViewModel by viewModels()
    var currentNav = mutableListOf<Int>()
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
        viewModel.currentNav.observe(viewLifecycleOwner){
            currentNav = it
        }

        view.setOnKeyListener { _, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN){
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
    viewModel.firstMenuDraw()

    if(currentMenu!=null){
        for(item in currentMenu!!){
            when(item.typeId){
                1-> FirstMenuItem(fontSize = fontSize!!, header = item.header!!, text =item.text!! , imgId = item.icon!!,submenuId = item.subMenuId!!)
                2-> SecondMenuItem(fontSize = fontSize!!, header = item.header!!, submenuId = item.subMenuId!!)
                3-> ThirdMenuItem(fontSize = fontSize!!, text = item.text!!, id = item.id!!, submenuId = item.subMenuId!!)
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        color = colorResource(id = R.color.library_top_bar_header)
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                modifier = Modifier.aspectRatio(5.8f, false),
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_2),
                        tint = colorResource(id = R.color.library_top_bar_fav),
                        contentDescription = "Избранное",
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )
                }
            )
        },
        backgroundColor = Color(0x00000000)
    ) {
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(state = scrollState,enabled = true)) {
            ItemData()
        }
    }
}

@Composable
fun FirstMenuItem(fontSize: Float, header: String, text: String, imgId: Int, submenuId: Int) {
    val viewModel: LibraryViewModel = viewModel()
    Row(
        Modifier
            .aspectRatio(3.8f, true)
            .clickable {
                viewModel.setCurrentMenu(submenuId)
                viewModel.addNavAction(submenuId)
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
                Image(
                    painter = painterResource(id = imgId),
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
                        style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
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
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

@Composable
fun SecondMenuItem(fontSize: Float, header: String, submenuId: Int) {
    val viewModel: LibraryViewModel = viewModel()
    Row(
        Modifier
            .aspectRatio(6.3f, true)
            .clickable {
                viewModel.setCurrentMenu(submenuId)
                viewModel.addNavAction(submenuId)
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
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ThirdMenuItem(fontSize: Float, text: String, id: Int,submenuId: Int) {
    val viewModel: LibraryViewModel = viewModel()
    Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.clickable {
        viewModel.clearNavAction()
        viewModel.setCurrentMenu(submenuId)
    }) {
        Box(Modifier.aspectRatio(13.8f))
        Text(
            text = id.toString(),
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
    }
}