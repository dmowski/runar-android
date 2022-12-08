package com.tnco.runar.ui.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.tnco.runar.R

@Composable
internal fun RuneDescription(
    fontSize: Float,
    header: String,
    text: String,
    imgLink: String,
    runeTags: List<String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.aspectRatio(30f))
        Text(
            text = header,
            color = colorResource(id = R.color.lib_run_header),
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { ((fontSize * 1.2f)).toSp() },
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
            Image(
                painter = rememberImagePainter(imgLink, builder = {
                    size(OriginalSize)
                }),
                contentDescription = null,
                modifier = Modifier
                    .background(Color(0x00000000))
                    .weight(120f)
                    .fillMaxWidth()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(152f)
            )
        }
        Box(Modifier.aspectRatio(30f))
        FlowRow {
            for (tag in runeTags) {
                if (tag.isNotEmpty()) {
                    Text(
                        text = tag,
                        style = TextStyle(
                            color = colorResource(R.color.lib_rune_tag_text),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(12.dp, 6.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.lib_rune_tag_border),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(12.dp, 6.dp)
                    )
                }
            }
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
                            fontSize = with(LocalDensity.current) { ((fontSize * 0.95f)).toSp() },
                            textAlign = TextAlign.Start,
                            lineHeight = with(LocalDensity.current) { ((fontSize * 1.4f)).toSp() }
                        ),
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
