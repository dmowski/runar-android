package com.tnco.runar.ui.screenCompose

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R
import com.tnco.runar.ui.screenCompose.componets.AppBar
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.rectShadow

@Composable
fun GeneratorFinalScreen(navController: NavController) {
    GeneratorFinal(navController, viewModel = MainViewModel())
}

@Composable
private fun GeneratorFinal(navController: NavController, viewModel: MainViewModel) {
    val viewModel: MainViewModel = hiltViewModel()
    Log.d("ViewModel", "Adress = $viewModel")
    val image = viewModel.backgroundInfo.first { it.isSelected }.img!!

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.run_pattern),
            )
        },
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .padding(
                            top = 40.dp,
                            bottom = 150.dp,
                            start = 53.dp,
                            end = 53.dp
                        )
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                colorResource(id = R.color.generator_card_border)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .rectShadow(
                            cornersRadius = 20.dp,
                            shadowBlurRadius = 20.dp,
                            offsetY = 6.dp
                        ),
                    shape = RoundedCornerShape(20.dp),
                    color = colorResource(id = R.color.audio_image_background)

                ) {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = null
                    )
                }
                Row(
                    modifier = Modifier.padding(
                        start = 53.dp,
                        top = 600.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(CornerSize(5.dp)))
                            .background(colorResource(id = R.color.generator_btns)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.save_image),
                            contentDescription = stringResource(id = R.string.generator)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(60.dp)
                            .clip(RoundedCornerShape(CornerSize(5.dp)))
                            .background(colorResource(id = R.color.generator_btns)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.share_image),
                            contentDescription = stringResource(id = R.string.generator)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GeneratorFinalScreenPreview() {
    GeneratorFinalScreen(rememberNavController())
}
