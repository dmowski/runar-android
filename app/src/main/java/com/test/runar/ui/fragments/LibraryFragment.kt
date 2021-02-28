package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.runar.R
import com.test.runar.presentation.viewmodel.LibraryViewModel

class LibraryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Bars()
            }
        }
    }
}

@Composable
fun ItemData() {
    val viewModel: LibraryViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()

    FirstMenuItem(
        fontSize!!,
        stringResource(id = R.string.library_item1_header),
        stringResource(id = R.string.library_item1_text),
        R.drawable.library_item1_pic
    )
    FirstMenuItem(
        fontSize!!,
        stringResource(id = R.string.library_item2_header),
        stringResource(id = R.string.library_item2_text),
        R.drawable.library_item2_pic
    )
    FirstMenuItem(
        fontSize!!,
        stringResource(id = R.string.library_item3_header),
        stringResource(id = R.string.library_item3_text),
        R.drawable.library_item3_pic
    )
    FirstMenuItem(
        fontSize!!,
        stringResource(id = R.string.library_item4_header),
        stringResource(id = R.string.library_item4_text),
        R.drawable.library_item4_pic
    )
    FirstMenuItem(
        fontSize!!,
        stringResource(id = R.string.library_item5_header),
        stringResource(id = R.string.library_item5_text),
        R.drawable.library_item5_pic
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ItemData()
}

@Composable
fun Bars() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        color = colorResource(id = R.color.library_top_bar_header)
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                modifier = Modifier.aspectRatio(5.8f, false),
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_2),
                        tint = colorResource(id = R.color.library_top_bar_fav),
                        contentDescription = "Избранное",
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )
                }
            )
        },
        backgroundColor = Color(0x00000000)
    ) {
        Column {
            ItemData()
        }
    }
}

@Composable
fun FirstMenuItem(fontSize: Float, header: String, text: String, imgId: Int) {
    Row(
        Modifier
            .aspectRatio(3.8f, true)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        )
        Column(
            Modifier
                .fillMaxSize()
                .weight(398f)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(20f)
            )
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(62f), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imgId),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .padding(top = 5.dp, bottom = 5.dp)
                )
                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(277f)
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.library_item_header),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
                    )
                    Text(
                        text = text,
                        color = colorResource(id = R.color.library_item_text),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 0.8).toFloat()).toSp() })
                    )
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(17f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .weight(10f)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(16f)
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(20f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_divider),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}