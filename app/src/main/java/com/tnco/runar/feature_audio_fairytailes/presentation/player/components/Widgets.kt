package com.tnco.runar.feature_audio_fairytailes.presentation.player.components

import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.tnco.runar.R
import com.tnco.runar.ui.fragment.LibraryFragmentDirections

@Composable
internal fun AudioHeader(
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
                    lineHeight = 24.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Divider(
                color = colorResource(id = R.color.audio_divider),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
internal fun AudioDetailRow(
    audioName: String,
    audioGroup: String,
    image: ImageVector = Icons.Filled.LibraryMusic,
    time: String = "3:27",
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val direction = LibraryFragmentDirections
                    .actionLibraryFragmentToAudioDetailsFragment(audioName, audioGroup)
                navController.navigate(direction)
            }
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp
            ),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                val (audioImage, audioText, audioTime) = createRefs()

                Image(
                    painter = rememberVectorPainter(image = image),
                    contentDescription = "Audio Image",
                    modifier = Modifier
                        .constrainAs(audioImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(64.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.audio_border_image),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Fit
                )
                Box(
                    modifier = Modifier
                        .constrainAs(audioText) {
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                            top.linkTo(audioImage.top)
                            bottom.linkTo(audioImage.bottom)
                            start.linkTo(audioImage.end, 16.dp)
                            end.linkTo(audioTime.start, 16.dp)
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = audioName,
                        style = TextStyle(
                            color = colorResource(id = R.color.audio_name_text),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(R.font.roboto_regular)
                            ),
                            lineHeight = 24.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    modifier = Modifier
                        .constrainAs(audioTime) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end, 16.dp)
                        },
                    text = time,
                    style = TextStyle(
                        color = colorResource(id = R.color.audio_time_text),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ),
                        lineHeight = 24.sp
                    )
                )
            }
            Divider(
                modifier = Modifier
                    .padding(
                        start = 80.dp
                    ),
                color = colorResource(id = R.color.audio_divider),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
internal fun AudioAppBar(
    title: String,
    icon: ImageVector = Icons.Filled.ArrowBack,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    color = colorResource(id = R.color.audio_top_app_bar_header),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                )
            )
        },
        backgroundColor = colorResource(id = R.color.audio_top_app_bar),
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Arrow Back",
                    tint = colorResource(id = R.color.audio_top_app_bar_header)
                )
            }
        },
        elevation = 0.dp
    )
}

@Preview
@Composable
private fun AudioDetailRowPreview() {
    // AudioDetailRow(name = "Сказка о пряничке")
}

@Preview
@Composable
private fun HeaderAudioDetailRowPreview() {
    AudioHeader(name = "Норвежские сказки")
}
