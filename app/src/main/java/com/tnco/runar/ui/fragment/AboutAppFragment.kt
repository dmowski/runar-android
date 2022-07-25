package com.tnco.runar.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tnco.runar.R
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.viewmodel.AboutViewModel

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
    Column {
        Box(Modifier.aspectRatio(16f))
        Row {

            val annotatedLinkString: AnnotatedString = buildAnnotatedString {
                val string = stringResource(id = R.string.about_txt)
                val first = string.indexOf("https")
                val second = string.indexOf("https", first + 1)
                append(string)
                addStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.url_text_about_color)
                    ), start = first, end = first + 35
                )
                addStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.url_text_about_color)
                    ), start = second, end = string.length
                )

                addStyle(
                    style = ParagraphStyle(
                        lineHeight = with(LocalDensity.current) { ((fontSize!! * 1.4).toFloat()).toSp() }
                    ), start = 0, end = string.length
                )

                addStringAnnotation(
                    tag = "URL",
                    annotation = stringResource(id = R.string.about_url1),
                    start = first,
                    end = first + 35
                )

                addStringAnnotation(
                    tag = "URL",
                    annotation = stringResource(id = R.string.about_url2),
                    start = second,
                    end = string.length
                )
            }
            val uriHandler = LocalUriHandler.current

            Box(
                Modifier
                    .fillMaxSize()
                    .weight(24f)
            )
            ClickableText(
                text = annotatedLinkString,
                style = TextStyle(
                    fontSize = with(LocalDensity.current) { ((fontSize!! * 0.95).toFloat()).toSp() },
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    color = colorResource(
                        id = R.color.about_text_color
                    )
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(366f),
                onClick = {
                    annotatedLinkString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                }
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(24f)
            )
        }
    }
}
