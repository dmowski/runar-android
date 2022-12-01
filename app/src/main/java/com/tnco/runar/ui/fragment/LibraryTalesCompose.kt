package com.tnco.runar.ui.fragment

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.tnco.runar.R

@Composable
internal fun SimpleTextItem(fontSize: Float, text: String?, urlTitle: String?, urlLink: String?) {
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
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) { ((fontSize * 0.95f)).toSp() },
                        textAlign = TextAlign.Start,
                        lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }),
                )
            }
            if (!urlLink.isNullOrEmpty() && !urlTitle.isNullOrEmpty()) {
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
                        style = ParagraphStyle(
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.05f)).toSp() }
                        ), start = 0, end = annotatedText.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = urlLink,
                        start = urlTitle.length + 1,
                        end = annotatedText.length
                    )

                }
                val uriHandler = LocalUriHandler.current
                ClickableText(
                    text = annotatedLinkString,
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) { ((fontSize * 0.7f)).toSp() },
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