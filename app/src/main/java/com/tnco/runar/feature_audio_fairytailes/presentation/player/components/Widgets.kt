package com.tnco.runar.feature_audio_fairytailes.presentation.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tnco.runar.R

@Composable
fun Header(name: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = Color.Black
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = name,
                style = TextStyle(
                    color = Color(0xFFACACAC),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(R.font.roboto_regular)
                    ),
                    lineHeight = 24.em
                )
            )
        }
    }
}

@Composable
fun AudioDetailRow(name: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color.Black
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {

            ConstraintLayout(Modifier.fillMaxWidth()) {

                val(audioImage, audioText, audioTime, audioImageMore) = createRefs()

                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.LibraryMusic),
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
                            Color(0xFFD2C4AD),
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
                        color = Color(0xFFE1E1E1),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ),
                        lineHeight = 24.em
                    )
                )
                Text(
                    modifier = Modifier
                        .constrainAs(audioTime) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(audioImageMore.start, 16.dp)
                        },
                    text = "3:27",
                    style = TextStyle(
                        color = Color(0xFFE1E1E1),
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
                    colorFilter = ColorFilter.tint(Color(0xFF6E6E6E))
                )
            }
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
    Header(name = "Норвежские сказки")
}
