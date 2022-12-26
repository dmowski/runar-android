package com.tnco.runar.feature_audio_fairytailes.presentation.player

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tnco.runar.R
import com.tnco.runar.feature_audio_fairytailes.presentation.player.components.AudioAppBar
import com.tnco.runar.util.rectShadow

@Composable
fun AudioScreenDetails(navController: NavController) {
    MainScreen(navController)
}

@Composable
private fun MainScreen(navController: NavController) {
    val audioName = navController
        .currentBackStackEntry?.arguments?.getString("audioName") ?: ""
    val audioGroup = navController
        .currentBackStackEntry?.arguments?.getString("audioGroup") ?: ""

    Scaffold(
        topBar = {
            AudioAppBar(
                title = "",
                navController = navController
            )
        },
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .padding(
                        start = 54.dp,
                        end = 54.dp,
                        top = 16.dp,
                        bottom = 32.dp
                    )
                    .aspectRatio(1f)
                    .border(
                        1.dp,
                        colorResource(id = R.color.audio_border_image).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .rectShadow(
                        cornersRadius = 32.dp,
                        shadowBlurRadius = 8.dp,
                        offsetX = 8.dp,
                        offsetY = 8.dp
                    ),
                shape = RoundedCornerShape(32.dp),
                color = colorResource(id = R.color.audio_image_background).copy(alpha = 0.8f)
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.fillMaxSize(0.5f),
                        imageVector = Icons.Filled.LibraryMusic,
                        contentDescription = "Detail Image",
                        tint = Color.White
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(
                        start = 54.dp,
                        end = 54.dp
                    ),
                text = audioName,
                style = TextStyle(
                    color = colorResource(id = R.color.white_f8),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                ),
            )

            Text(
                modifier = Modifier
                    .padding(
                        start = 54.dp,
                        end = 54.dp
                    ),
                text = audioGroup,
                style = TextStyle(
                    color = colorResource(id = R.color.white_f8),
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center
                )
            )

            Row(
                modifier = Modifier
                    .padding(top = 56.dp)
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_previous),
                        contentDescription = "Skip Previous",
                        tint = colorResource(id = R.color.audio_play_panel)
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.replay_15),
                        contentDescription = "Replay 15",
                        tint = colorResource(id = R.color.audio_play_panel)
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_circle_button),
                        contentDescription = "Play Button",
                        tint = colorResource(id = R.color.audio_play_button)
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.forward_15),
                        contentDescription = "Forward 15",
                        tint = colorResource(id = R.color.audio_play_panel)
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next),
                        contentDescription = "Skip Next",
                        tint = colorResource(id = R.color.audio_play_panel)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(
                        start = 30.dp,
                        end = 30.dp,
                        top = 20.dp,
                        bottom = 96.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2:22",
                    style = TextStyle(
                        color = colorResource(id = R.color.white_f8),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        lineHeight = 18.sp
                    )
                )

                Slider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f, true),
                    value = 0.3f,
                    onValueChange = { },
                    colors = SliderDefaults.colors(
                        thumbColor = colorResource(id = R.color.audio_thumb_slider),
                        activeTrackColor = colorResource(id = R.color.audio_thumb_slider),
                        inactiveTrackColor = colorResource(id = R.color.white_f8)
                    )
                )

                Text(
                    text = "5:56",
                    style = TextStyle(
                        color = colorResource(id = R.color.white_f8),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun AudioScreenDetailsPreview() {
    // AudioScreenDetails()
}
