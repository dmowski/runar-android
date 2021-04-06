package com.tnco.runar.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tnco.runar.R
import com.tnco.runar.presentation.viewmodel.SettingsViewModel
import com.tnco.runar.ui.Navigator

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by viewModels()
    private var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.updateMusicStatus()
        viewModel.updateLocaleData()
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        navigator = context as Navigator
        super.onAttach(context)
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                Bars(navigator!!)
            }
        }
        return view
    }
}

@Composable
private fun Bars(navigator: Navigator) {
    val viewModel: SettingsViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val musicStatus by viewModel.musicStatus.observeAsState()
    val languagePos by viewModel.selectedLanguagePos.observeAsState()
    val headerUpdater by viewModel.headerUpdater.observeAsState()

    var header = ""

    header = if (headerUpdater!!) stringResource(id = R.string.settings_layout)
    else stringResource(id = R.string.settings_layout)

    val context = LocalContext.current



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.library_top_bar_header),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize!! * 1.35).toFloat()).toSp() })
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar)
            )
        },
        backgroundColor = Color(0x73000000)
    ) {
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(state = scrollState, enabled = true)) {
            EmptyMenuItem()
            LangMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.settings_language),
                selectedPos = languagePos!!
            )
            SwitcherMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.music_txt),
                checkAction = { viewModel.changeMusicStatus(it) },
                state = musicStatus!!
            )
            SimpleMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.rate_app_txt),
                clickAction = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(
                            "https://play.google.com/store/apps/details?id=com.tnco.runar"
                        ) // here is the uri  app in google play
                        setPackage("com.android.vending")
                    }
                    context.startActivity(intent)
                })
            SimpleMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.about_app_txt),
                clickAction = { navigator.navigateToAboutFragment() })
        }
    }
}

@Composable
private fun SimpleMenuItem(fontSize: Float, header: String, clickAction: () -> Unit) {
    Row(
        Modifier
            .aspectRatio(6.3f, true)
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
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(66f), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.library_item_header),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
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
            Divider(
                color = Color(0xA6545458)
            )
        }
    }
}

@Composable
private fun SwitcherMenuItem(
    fontSize: Float,
    header: String,
    checkAction: ((Boolean) -> Unit),
    state: Boolean
) {
    Row(
        Modifier
            .aspectRatio(6.3f, true)
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
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() }),
                    modifier = Modifier
                        .weight(320f)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(17f)
                )
                Switch(
                    checked = state,
                    onCheckedChange = checkAction,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.switcher_checked_thumb),
                        checkedTrackColor = colorResource(id = R.color.switcher_checked_back),
                        uncheckedThumbColor = colorResource(id = R.color.switcher_unchecked_thumb),
                        uncheckedTrackColor = colorResource(id = R.color.switcher_unchecked_back),
                    )
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(12f)
                )
            }
            Divider(
                color = Color(0xA6545458)
            )
        }
    }
}

@Composable
private fun LangMenuItem(fontSize: Float, header: String, selectedPos: Int) {
    val langList = arrayListOf<String>(
        stringResource(id = R.string.settings_language_rus),
        stringResource(id = R.string.settings_language_en)
    )
    Row(
        Modifier.fillMaxSize()
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
            Text(
                text = header,
                color = colorResource(id = R.color.settings_language_name),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
            )
            for (i in 0 until langList.size) {
                if (i == selectedPos) {
                    LanguageItem(
                        fontSize = fontSize,
                        itemName = langList[i],
                        selected = true,
                        pos = i
                    )
                } else {
                    LanguageItem(
                        fontSize = fontSize,
                        itemName = langList[i],
                        selected = false,
                        pos = i
                    )
                }
            }
            Box(
                Modifier
                    .aspectRatio(35f, true)
            )
            Divider(
                color = Color(0xA6545458)
            )
        }
    }
}

@Composable
private fun LanguageItem(fontSize: Float, itemName: String, selected: Boolean, pos: Int) {
    val viewModel: SettingsViewModel = viewModel()
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxSize()
            .aspectRatio(11f), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = itemName,
            color = colorResource(id = R.color.settings_language),
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.8).toFloat()).toSp() }),
            modifier = Modifier
                .weight(336f)
        )
        Box(
            Modifier
                .fillMaxSize()
                .weight(17f)
        )
        RadioButton(
            selected = selected,
            onClick = { if (!selected) viewModel.changeLanguage(pos, (context as Activity)) },
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.switcher_checked_thumb),
                unselectedColor = colorResource(id = R.color.switcher_unchecked_thumb)
            )
        )
        Box(
            Modifier
                .fillMaxSize()
                .weight(12f)
        )
    }
}

@Composable
private fun EmptyMenuItem() {
    Box(
        Modifier
            .aspectRatio(14f, true)
    )
}