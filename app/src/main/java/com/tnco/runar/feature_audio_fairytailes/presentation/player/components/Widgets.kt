package com.tnco.runar.feature_audio_fairytailes.presentation.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tnco.runar.R

@Composable
fun AudioHeader(
    name: String,
    isFirstHeader: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (isFirstHeader) 28.dp else 12.dp
            ),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp
                    ),
                text = name,
                style = TextStyle(
                    color = colorResource(id = R.color.audio_header_text),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    lineHeight = 24.em
                )
            )
            Divider(
                color = colorResource(id = R.color.audio_divider),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
fun AudioDetailRow(
    name: String,
    image: ImageVector = Icons.Filled.LibraryMusic,
    time: String = "3:27"
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp
            ),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                val (audioImage, audioText, audioTime, audioImageMore) = createRefs()

                Image(
                    painter = rememberVectorPainter(image = image),
                    contentDescription = "Audio Image",
                    modifier = Modifier
                        .constrainAs(audioImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(40.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.audio_border_image),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier
                        .constrainAs(audioText) {
                            width = Dimension.fillToConstraints
                            top.linkTo(parent.top)
                            start.linkTo(audioImage.end, 16.dp)
                            end.linkTo(audioTime.start, 16.dp)
                        },
                    text = name,
                    style = TextStyle(
                        color = colorResource(id = R.color.audio_name_text),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ),
                        lineHeight = 24.em
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .constrainAs(audioTime) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(audioImageMore.start, 16.dp)
                        },
                    text = time,
                    style = TextStyle(
                        color = colorResource(id = R.color.audio_time_text),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ),
                        lineHeight = 24.em
                    )
                )
                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.MoreVert),
                    contentDescription = "Image More",
                    modifier = Modifier
                        .constrainAs(audioImageMore) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(24.dp),
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.audio_image_more_tint))
                )
            }
            Divider(
                modifier = Modifier
                    .padding(
                        start = 56.dp
                    ),
                color = colorResource(id = R.color.audio_divider),
                thickness = 0.5.dp
            )
        }
    }
}

@Preview
@Composable
fun AudioDetailRowPreview() {
    AudioDetailRow(name = "Сказка о пряничке")
}

@Preview
@Composable
fun HeaderAudioDetailRowPreview() {
    AudioHeader(name = "Норвежские сказки")
}
