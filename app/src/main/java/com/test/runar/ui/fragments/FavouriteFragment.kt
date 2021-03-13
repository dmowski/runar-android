package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.presentation.viewmodel.FavouriteViewModel
import com.test.runar.presentation.viewmodel.LibraryViewModel
import dev.chrisbanes.accompanist.coil.CoilImage

class FavouriteFragment : Fragment() {
    val viewModel: FavouriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.getUserLayoutsFromDB()
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

        view.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //viewModel.goBackInMenu()
                }
            }
            true
        }
        return view
    }
}

@Composable
private fun Bars() {
    val viewModel: FavouriteViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val favData by viewModel.favData.observeAsState()
    val existSelected by viewModel.haveSelectedItem.observeAsState()
    var barColor = colorResource(id = R.color.library_top_bar_header)
    var barFont = FontFamily(Font(R.font.roboto_medium))
    var barFontSize = with(LocalDensity.current) { ((fontSize!! * 1.35).toFloat()).toSp() }
    var barText = "Избранное"
    var navIcon: @Composable() (() -> Unit)? = null
    var navActions: @Composable RowScope.() -> Unit = {}

    val checkedState = remember { mutableStateOf(false) }

    if (existSelected!!) {
        barText = ""
        navIcon = { TopBarIcon(clickAction = {
            viewModel.changeAll(false)
            checkedState.value = false
        }) }
        navActions = { TopBarActions() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = barText,
                        color = barColor,
                        fontFamily = barFont,
                        style = TextStyle(fontSize = barFontSize)
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                navigationIcon = navIcon,
                actions = navActions
            )
        },
        backgroundColor = Color(0x73000000)
    ) {

        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(state = scrollState, enabled = true)) {
            checkboxItem(
                state = checkedState.value,
                checkAction = {
                    checkedState.value = it
                    viewModel.changeAll(it)
                }
            )
            if (favData != null) {
                for (item in favData!!) {
                    if (item.id != 666999) {
                        FavItem(
                            fontSize = fontSize!!,
                            header = item.header!!,
                            text = item.content!!,
                            imgId = 12,
                            clickAction = {},
                            state = item.selected!!,
                            checkAction = {
                                viewModel.changeSelection(item.id!!)
                            })
                    }
                }
            }
            Box(modifier = Modifier.aspectRatio(15f, true))
        }
    }
}

@Composable
private fun TopBarIcon(clickAction: () -> Unit) {
    IconButton(onClick = clickAction) {
        Icon(
            painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
            tint = colorResource(id = R.color.library_top_bar_fav),
            contentDescription = "arrow"
        )
    }
}

@Composable
private fun TopBarActions() {
    //val viewModel: LibraryViewModel = viewModel()
    IconButton(onClick = { /*viewModel.goBackInMenu() */ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            tint = colorResource(id = R.color.fav_top_bar_delete),
            contentDescription = "trash"
        )
    }
}

@Composable
private fun checkboxItem(
    state: Boolean,
    checkAction: (Boolean) -> Unit
) {
    Box(
        Modifier
            .aspectRatio(10f, true)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(355f)
            )
            Checkbox(
                checked = state,
                onCheckedChange = checkAction,
                modifier = Modifier
                    .weight(18f),
                colors = CheckboxDefaults.colors(
                    uncheckedColor = colorResource(id = R.color.fav_checkbox_border),
                    checkedColor = colorResource(id = R.color.fav_checkbox_selected),
                    checkmarkColor = colorResource(id = R.color.fav_checkbox_checkmark)
                )

            )
            //end space
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(22f)
            )
        }
    }
}

@Composable
private fun FavItem(
    fontSize: Float,
    header: String,
    text: String,
    imgId: Int,
    clickAction: () -> Unit,
    state: Boolean,
    checkAction: (Boolean) -> Unit
) {

    Row(
        Modifier
            .aspectRatio(4.6f, true)
            .clickable(onClick = clickAction)
    ) {
        //start space
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
        //data space
        Column(
            Modifier
                .fillMaxSize()
                .weight(398f)
        ) {
            //top space
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(10f)
            )
            //data
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(66f), verticalAlignment = Alignment.CenterVertically
            ) {
                //first img
                Image(
                    painter = painterResource(id = R.drawable.fav_test_img),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .padding(top = 5.dp, bottom = 5.dp)
                )
                //space between first img and text
                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(268f)
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.fav_time_text),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.8).toFloat()).toSp() }),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = text,
                        color = colorResource(id = R.color.fav_inter_text),
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.8).toFloat()).toSp() })
                    )
                }
                //space between text and end img
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(15f)
                )
                //end img
                Checkbox(
                    checked = state,
                    onCheckedChange = checkAction,
                    modifier = Modifier
                        .weight(18f),
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = colorResource(id = R.color.fav_checkbox_border),
                        checkedColor = colorResource(id = R.color.fav_checkbox_selected),
                        checkmarkColor = colorResource(id = R.color.fav_checkbox_checkmark)
                    )

                )
                //end space
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(22f)
                )
            }
            //bottom space
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(10f)
            )
            //bottom divider
            Image(
                painter = painterResource(id = R.drawable.ic_divider),
                contentDescription = null
            )
        }
    }
}
