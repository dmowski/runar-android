package com.tnco.runar.ui.screenCompose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.tnco.runar.R
import com.tnco.runar.ui.screenCompose.componets.AppBar
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.rectShadow

@Composable
fun GenFinal(navController: NavController) {
    GeneratorFinalScreen(navController)
}

@Composable
private fun GeneratorFinalScreen(navController: NavController) {
    val viewModel = viewModel<MainViewModel>()
    val image = viewModel.backgroundInfo.first { it.isSelected }.img!!
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.run_pattern),
                navController = navController
            )
        },
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {

            val (onClickSave) = createRefs()

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
                    AsyncImage(
                        model = image,
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
                            .clickable { onClickSave }
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
                            .clickable { onClickSave }
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
