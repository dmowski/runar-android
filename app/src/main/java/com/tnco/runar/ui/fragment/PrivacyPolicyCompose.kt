package com.tnco.runar.ui.fragment

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tnco.runar.R

private const val EDGE_WIDTH = 32

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrivacyPolicyFragmentLayout(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .clickable(onClick = { navController.popBackStack() })
                        )
                        Text(
                            text = stringResource(id = R.string.privacy_policy_title),
                            color = colorResource(id = R.color.library_top_bar_header),
                            fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                            style = TextStyle(fontSize = 36.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = EDGE_WIDTH.dp, end = 12.dp)
                                .widthIn(max = EDGE_WIDTH.dp * 4)
                                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                                .drawWithContent {
                                    drawContent()
                                    drawFadedEdge(leftEdge = true)
                                    drawFadedEdge(leftEdge = false)
                                }
                                .basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    spacing = MarqueeSpacing(0.dp))
                                .padding(top = 6.dp, start = EDGE_WIDTH.dp, end = 12.dp)
                        )
                    }
                },
                backgroundColor = colorResource(id = R.color.transparent),
                elevation = 0.dp,
            )
        },
        backgroundColor = colorResource(id = R.color.library_top_bar_2),
    ) {
        val scrollState = rememberScrollState()
        if (scrollState.isScrollInProgress && scrollState.value > 0) {
            ScrollBars(scrollState)
        }
        Column(
            Modifier
                .verticalScroll(state = scrollState, enabled = true)
                .padding(all = dimensionResource(id = R.dimen.about_app_padding_policy))
        ) {
            Text(
                text = stringResource(id = R.string.last_update),
                fontSize = 14.sp,
                color = colorResource(id = R.color.neutrals_gray_500),
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.conditions_notifications),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.personal_information),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.content_personal_information_1),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                text = stringResource(id = R.string.content_personal_information_2),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.app_do_not),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.app_do_not_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.information_process),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.information_process_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.storing_of_information),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.storing_of_information_content_1),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.storing_of_information_content_2),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.storing_of_information_content_3),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.legal_base),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.legal_base_content_1),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.legal_base_content_2),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.sharing_of_personal),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.sharing_of_personal_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.term_of_keeping),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.term_of_keeping_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.third_party),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.third_party_content_1),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                text = stringResource(id = R.string.third_party_content_2),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.controls_for),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.controls_for_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.notice_updates),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.notice_updates_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.contact_information),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.contact_information_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.review_update_delete),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.sacr_button_header),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = stringResource(id = R.string.review_update_delete_content),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.neutrals_gray_100),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp
                )
            )
        }
    }
}
private fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
    val edgeWidthPx = EDGE_WIDTH.dp.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(
                androidx.compose.ui.graphics.Color.Transparent,
                androidx.compose.ui.graphics.Color.Black
            ),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}
