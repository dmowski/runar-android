package com.tnco.runar.ui.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tnco.runar.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoticeBottomSheetGenerator(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    fontSize: Float,
    watchAD: () -> (Unit),
    purchaseSubsBtnClicked: () -> (Unit),
    onClose: () -> (Unit)
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .border(
                        2.dp,
                        colorResource(id = R.color.notice_border_color),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorResource(id = R.color.black_100_per),
                                colorResource(id = R.color.black_47_per),
                                colorResource(id = R.color.black_0_per)
                            )
                        )
                    )
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.monetization_close_button),
                        contentDescription = "Hide",
                        modifier = Modifier
                            .padding(end = 12.dp, top = 12.dp)
                            .align(Alignment.TopEnd)
                            .clickable(onClick = { onClose() }),
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.notice_stars),
                    contentDescription = "",
                    modifier = Modifier.offset(y = (-20).dp),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.generator_fog),
                    color = colorResource(id = R.color.neutrals_gray_100),
                    fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) {
                            (fontSize * 1.8f).toSp()
                        },
                        fontWeight = FontWeight.W700
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.rune_powers_not_enough),
                    color = colorResource(id = R.color.neutrals_gray_300),
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) {
                            fontSize.toSp()
                        },
                        fontWeight = FontWeight.W400
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = colorResource(id = R.color.bottom_sheet_subs_button)
                        )
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.bottom_sheet_ad_button)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        watchAD()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.watch_ad),
                        color = colorResource(id = R.color.run_draws_time_color),
                        fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) {
                                (fontSize * 1.4f).toSp()
                            },
                            fontWeight = FontWeight.W700
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.bottom_sheet_subs_button)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        purchaseSubsBtnClicked()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.purchase_subs),
                        color = colorResource(id = R.color.black),
                        fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) {
                                (fontSize * 1.4f).toSp()
                            },
                            fontWeight = FontWeight.W700
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    ) {}

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun NoticeBottomSheetGeneratorPreview() {
    NoticeBottomSheetGenerator(
        sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.HalfExpanded),
        coroutineScope = rememberCoroutineScope(),
        fontSize = 55f,
        watchAD = {},
        purchaseSubsBtnClicked = {}
    ) {}
}
