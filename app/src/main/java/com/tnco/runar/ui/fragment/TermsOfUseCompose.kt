package com.tnco.runar.ui.fragment

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R

private const val EDGE_WIDTH = 32

@Composable
fun TermsOfUseCompose(navController: NavController) {
    Scaffold(
        topBar = {
            com.tnco.runar.ui.screenCompose.componets.AppBar(
                title = stringResource(id = R.string.terms_of_use_title),
                navController = navController,
                showIcon = true
            )
        },
        backgroundColor = colorResource(id = R.color.transparent)
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
                text = stringResource(id = R.string.terms_of_use_predesc),
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
                text = stringResource(id = R.string.terms_of_use_definitions),
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
                text = stringResource(id = R.string.terms_of_use_desc_definitions),
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
                text = stringResource(id = R.string.terms_of_use_acknowledgment),
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
                text = stringResource(id = R.string.terms_of_use_desc_acknowledgment),
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
                text = stringResource(id = R.string.terms_of_use_copyright_policy),
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
                text = stringResource(id = R.string.terms_of_use_desc_copyright_policy),
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
                text = stringResource(id = R.string.terms_of_use_intellectual_property),
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
                text = stringResource(id = R.string.terms_of_use_desc_intellectual_property),
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
                text = stringResource(id = R.string.terms_of_use_your_feedback_to_us),
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
                text = stringResource(id = R.string.terms_of_use_desc_your_feedback_to_us),
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
                text = stringResource(id = R.string.terms_of_use_links_to_other_websites),
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
                text = stringResource(id = R.string.terms_of_use_desc_links_to_other_websites),
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
                text = stringResource(id = R.string.terms_of_use_governing_law),
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
                text = stringResource(id = R.string.terms_of_use_desc_governing_law),
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
                text = stringResource(id = R.string.terms_of_use_disputes_resolution),
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
                text = stringResource(id = R.string.terms_of_use_desc_disputes_resolution),
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
                text = stringResource(id = R.string.terms_of_use_for_european_union),
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
                text = stringResource(id = R.string.terms_of_use_desc_for_european_union),
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
                text = stringResource(id = R.string.terms_of_use_the_united_states),
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
                text = stringResource(id = R.string.terms_of_use_desc_the_united_states),
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
                text = stringResource(id = R.string.terms_of_use_severability),
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
                text = stringResource(id = R.string.terms_of_use_desc_severability),
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
                text = stringResource(id = R.string.terms_of_use_changes),
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
                text = stringResource(id = R.string.terms_of_use_desc_changes),
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
                text = stringResource(id = R.string.terms_of_use_contact_us),
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
                text = stringResource(id = R.string.terms_of_use_desc_contact_us),
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

@Preview
@Composable
fun TermsOfUseComposePreview() {
    TermsOfUseCompose(rememberNavController())
}
