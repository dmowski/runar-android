package com.tnco.runar.ui.fragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tnco.runar.R
import com.tnco.runar.ui.screenCompose.componets.AppBar

@Composable
fun PrivacyPolicyFragmentLayout(navController: NavController) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.privacy_policy_title),
                navController = navController,
                showIcon = true
            )
        },
        backgroundColor = colorResource(id = R.color.library_top_bar_2),
    ) {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .verticalScroll(state = scrollState, enabled = true)
                .padding(all = dimensionResource(id = R.dimen.about_app_padding))
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 6.dp),
                text = stringResource(id = R.string.privacy_policy_title_2),
                fontSize = 20.sp,
                color = colorResource(id = R.color.audio_play_button),
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 12.dp),
                text = stringResource(id = R.string.last_update),
                fontSize = 14.sp,
                color = colorResource(id = R.color.neutrals_gray_500),
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 12.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 4.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 8.dp, top = 18.dp),
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
                modifier = Modifier.padding(start = 6.dp, top = 18.dp),
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
