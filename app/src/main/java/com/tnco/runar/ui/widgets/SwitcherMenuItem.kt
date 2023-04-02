package com.tnco.runar.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.tnco.runar.R

@Composable
fun SwitcherMenuItem(
    fontSize: Float,
    header: String,
    checkAction: ((Boolean) -> Unit),
    state: Boolean,
    clickAction: () -> Unit
) {
    Row(
        Modifier
            .aspectRatio(7.5f)
            .clickable(onClick = clickAction)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .weight(335f)
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.library_item_header),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
                )
                Switch(
                    checked = state,
                    onCheckedChange = checkAction,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.switcher_checked_thumb),
                        checkedTrackColor = colorResource(id = R.color.switcher_checked_back),
                        uncheckedThumbColor = colorResource(id = R.color.switcher_unchecked_thumb),
                        uncheckedTrackColor = colorResource(id = R.color.switcher_unchecked_back),
                    )
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxSize()
                .weight(9f)
        )
    }
}

@Preview
@Composable
fun SwitcherMenuItemPreview() {
    SwitcherMenuItem(
        fontSize = 40f,
        header = "Preview",
        checkAction = {},
        state = true,
        clickAction = {}
    )
}
