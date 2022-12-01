package com.tnco.runar.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.tnco.runar.BuildConfig
import com.tnco.runar.R
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by viewModels()
    private var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.updateMusicStatus()
        viewModel.updateLocaleData()
        viewModel.updateOnboardingStatus()
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
                Bars(navigator!!, findNavController())
            }
        }
        return view
    }
}

@Composable
private fun Bars(navigator: Navigator, navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    val musicStatus by viewModel.musicStatus.observeAsState()
    val onboardingStatus by viewModel.onboardingStatus.observeAsState()
    val languagePos by viewModel.selectedLanguagePos.observeAsState()
    val headerUpdater by viewModel.headerUpdater.observeAsState()

    val header = if (headerUpdater!!) stringResource(id = R.string.settings_layout)
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
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) {
                                ((fontSize!! * 1.35).toFloat()).toSp()
                            }
                        )
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar)
            )
        },
        backgroundColor = colorResource(id = R.color.settings_top_app_bar)
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState, enabled = true)
                .padding(
                    start = dimensionResource(id = R.dimen.settings_padding_left),
                    top = dimensionResource(id = R.dimen.settings_padding_top),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            LangMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.settings_language),
                selectedPos = languagePos!!
            )
            DividerItem()
            SwitcherMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.music_txt),
                checkAction = {
                    viewModel.changeMusicStatus(it)
                    if (it) navigator.getAudioFocus()
                    else navigator.dropAudioFocus()
                },
                state = musicStatus!!,
                clickAction = {
                    viewModel.changeMusicStatus(!musicStatus!!)
                    if (!musicStatus!!) navigator.getAudioFocus()
                    else navigator.dropAudioFocus()
                }
            )
            DividerItem()
            SwitcherMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.onboarding_txt),
                checkAction = {
                    viewModel.changeOnboardingStatus(it)
                },
                state = onboardingStatus!!,
                clickAction = {
                    viewModel.changeOnboardingStatus(!onboardingStatus!!)
                }
            )
            DividerItem()
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
                }
            )
            DividerItem()
            SimpleMenuItem(
                fontSize = fontSize!!,
                header = stringResource(id = R.string.about_app_txt),
                clickAction = {
                    val direction = SettingsFragmentDirections
                        .actionSettingsFragmentToAboutAppFragment()
                    navController.navigate(direction)
                }
            )
            DividerItem()
            if (BuildConfig.DEBUG) {
                SimpleMenuItem(
                    fontSize = fontSize!!,
                    header = stringResource(id = R.string.developer_options_title),
                    clickAction = {
                        val direction = SettingsFragmentDirections
                            .actionSettingsFragmentToDeveloperOptionsFragment()
                        navController.navigate(direction)
                    }
                )
                DividerItem()
            }
        }
    }
}

@Composable
private fun SimpleMenuItem(fontSize: Float, header: String, clickAction: () -> Unit) {
    Row(
        Modifier
            .aspectRatio(7.5f)
            .clickable(onClick = clickAction)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .weight(323f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = header,
                color = colorResource(id = R.color.library_item_header),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() }),
            )
            Image(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = null,
                modifier = Modifier
                    .background(colorResource(id = R.color.transparent))
            )
        }
        Spacer(
            Modifier
                .fillMaxSize()
                .weight(21f)
        )
    }
}

@Composable
private fun SwitcherMenuItem(
    fontSize: Float,
    header: String,
    checkAction: ((Boolean) -> Unit),
    state: Boolean,
    clickAction: () -> Unit
) {
    Row(
        Modifier
            .aspectRatio(7.5f)
            .clickable(onClick = clickAction)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .weight(335f)
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.library_item_header),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
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
            }
        }
        Spacer(
            Modifier
                .fillMaxSize()
                .weight(9f)
        )
    }
}

@Composable
private fun LangMenuItem(fontSize: Float, header: String, selectedPos: Int) {
    val langList: List<String> = arrayListOf(
        stringResource(id = R.string.settings_language_rus),
        stringResource(id = R.string.settings_language_en)
    )
    Row(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .weight(342f)
        ) {
            Row(
                Modifier
                    .aspectRatio(7.5f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.settings_language_name),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
                )
            }

            for (i in langList.indices) {
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
        }
        Spacer(
            Modifier
                .fillMaxSize()
                .weight(2f)
        )
    }
}

@Composable
private fun LanguageItem(fontSize: Float, itemName: String, selected: Boolean, pos: Int) {
    val viewModel: SettingsViewModel = viewModel()
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxSize()
            .aspectRatio(7.5f)
            .clickable(onClick = {
                if (!selected) viewModel.changeLanguage(
                    pos,
                    (context as Activity)
                )
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = itemName,
            color = colorResource(id = R.color.settings_language),
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    ((fontSize * 0.8).toFloat()).toSp()
                }
            ),
        )
        RadioButton(
            selected = selected,
            onClick = { if (!selected) viewModel.changeLanguage(pos, (context as Activity)) },
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.switcher_checked_thumb),
                unselectedColor = colorResource(id = R.color.switcher_unchecked_thumb)
            )
        )
    }
}

@Composable
private fun DividerItem() {
    Divider(
        color = colorResource(id = R.color.settings_divider)
    )
}
