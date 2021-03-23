package com.test.runar.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.runar.R
import com.test.runar.presentation.viewmodel.AboutViewModel
import com.test.runar.ui.Navigator

class AboutAppFragment : Fragment() {

    private var navigator: Navigator? = null

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
    val viewModel: AboutViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.about_app_txt),
                        color = colorResource(id = R.color.library_top_bar_header_2),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { fontSize!!.toSp() })
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                navigationIcon = { TopBarIcon(navigator) }
            )
        },
        backgroundColor = Color(0x73000000)
    ) {
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(state = scrollState, enabled = true)) {
            AboutText()
        }
    }
}

@Composable
private fun TopBarIcon(navigator: Navigator) {
    IconButton(onClick = { navigator.navigateToSettings() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
            tint = colorResource(id = R.color.library_top_bar_fav),
            contentDescription = "arrow"
        )
    }
}

@Composable
private fun AboutText() {
    val viewModel: AboutViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    Text(
        text = stringResource(id = R.string.about_txt),
        color = colorResource(id = R.color.about_text_color),
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize!! * 0.95).toFloat()).toSp() }),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 10.dp),
    )
}