package com.tnco.runar.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tnco.runar.R
import com.tnco.runar.ui.viewmodel.AboutViewModel

@Composable
fun AboutText() {
    val viewModel: AboutViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()
    Box {
        val annotatedLinkString: AnnotatedString = buildAnnotatedString {
            val string = stringResource(id = R.string.about_txt)
            val first = string.indexOf("https")
            val second = string.indexOf("https", first + 1)
            append(string)
            addStyle(
                style = SpanStyle(
                    color = colorResource(id = R.color.url_text_about_color)
                ),
                start = first,
                end = first + 35
            )
            addStyle(
                style = SpanStyle(
                    color = colorResource(id = R.color.url_text_about_color)
                ),
                start = second,
                end = string.length
            )

            addStyle(
                style = ParagraphStyle(
                    lineHeight = with(LocalDensity.current) {
                        ((fontSize!! * 1.57).toFloat()).toSp()
                    }
                ),
                start = 0,
                end = string.length
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

        ClickableText(
            text = annotatedLinkString,
            style = TextStyle(
                fontSize = with(LocalDensity.current) { fontSize!!.toSp() },
                fontFamily = FontFamily(Font(R.font.roboto_light)),
                color = colorResource(
                    id = R.color.about_text_color
                )
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

@Preview
@Composable
fun AboutTextPreview() {
    AboutText() // TODO handle with viewModel
}
